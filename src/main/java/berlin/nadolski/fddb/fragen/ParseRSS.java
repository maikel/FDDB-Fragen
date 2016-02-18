/*
 * Copyright 2016 Maikel Nadolski <maikel@nadolski.berlin>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package berlin.nadolski.fddb.fragen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class ParseRSS {
   private final static Logger logger = LogManager.getLogger(ParseRSS.class);
   static final String RSS_URL = "http://fddb.info/db/i18n/communityrss/?lang=de";
   static final String DEFAULT_JSON_FILE = "questions.json";

   /**
    * Send Email about updates of new questions and answers.
    * @param questions
    * @throws java.io.IOException
    * @throws freemarker.template.TemplateException
    */
   public static void print_html(List<Question> questions) throws IOException, TemplateException {
      Configuration cfg = new Configuration();
      cfg.setDefaultEncoding("UTF-8");
      cfg.setClassForTemplateLoading(ParseRSS.class, "/berlin/nadolski/fddb/fragen");
      Template temp = cfg.getTemplate("email.ftl");
      Map<String, Object> root = new HashMap<>();
      root.put("questions", questions);
      Writer out = new OutputStreamWriter(System.out);
      temp.process(root, out);
   }
   
   /**
    * Check for each new question if it is different or new to an older version.
    *
    * @param old_questions
    * @param new_questions
    *
    * @return diff_questions a list of all questions and answers which are in new_questions but not in old questions
    */
   public static List<Question> diff_questions(
           List<Question> old_questions,
           List<Question> new_questions) {
      LinkedList<Question> diff_questions = new LinkedList<>();
      for (Question new_q : new_questions) {
         // If the question is completely new add the hole thing to diff.
         int iq = old_questions.indexOf(new_q);
         if (iq == -1) {
            diff_questions.add(new_q);
            // If is is an already known question look for new answers and add them
            // to the diff if neccessary
         } else {
            Question old_q = old_questions.get(iq);
            if (old_q.getAnswers().size() < new_q.getAnswers().size()) {
               LinkedList<Answer> diff_answers = new LinkedList<>();
               for (Answer new_a : new_q.getAnswers()) {
                  if (!old_q.getAnswers().contains(new_a)) {
                     diff_answers.add(new_a);
                  }
               }
               diff_questions.add(
                       new Question(old_q.getTitle(), old_q.getHref(), old_q.getAuthor(),
                               diff_answers, old_q.getText(), old_q.getDate()));
            }
         }
      }
      return diff_questions;
   }

   /**
    * @param args the command line arguments
    *
    * @throws java.io.IOException
    */
   public static void main(String[] args) throws IOException, Exception {
      // if an argument is given, take it as path for the json file
      String json_filename;
      if (args.length > 0) {
         json_filename = args[0];
      } else {
         json_filename = DEFAULT_JSON_FILE;
      }
      
      List<Question> old_questions = null;
      List<Question> new_questions;
      ObjectMapper mapper = new ObjectMapper();
      // open JSON file
      Path json_path = Paths.get(json_filename);
      // if file exists open it and load old questions data.
      // TODO: check if path points to a directory and abort the program
      try (BufferedReader json_file = Files.newBufferedReader(json_path)) {
         old_questions = mapper.readValue(json_file, new TypeReference<List<Question>>() {
         });
      } catch (Exception exception) {
         logger.warn("No old questions found! Assume first time run.");
      }
      // Read the online rss feed with our parser class and get new list of questions
      Parser parser = new Parser(RSS_URL);
      new_questions = parser.questions();

      // write new questions into json file
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      try (BufferedWriter json_file = Files.newBufferedWriter(json_path)) {
         logger.debug("Writing new questions to data file!");
         mapper.writeValue(json_file, new_questions);
      }
      if (old_questions == null) {
         logger.info(new_questions.size() + " new questions.");
		 print_html(new_questions);
      } else {
         // show differences in questions and answers
         List<Question> diff_questions = diff_questions(old_questions, new_questions);
         if (diff_questions.size() > 0) {
            logger.info("Changes is in " + diff_questions.size() + " questions.");
            int num_new_answers = 0;
            num_new_answers = diff_questions.stream().map((q) -> q.getAnswers().size()).reduce(num_new_answers, Integer::sum);
            logger.info("Found " + num_new_answers + " new answers!");
            print_html(diff_questions);
         } else {
            logger.info("No new questions or answers.");
         }
      }
   }

}
