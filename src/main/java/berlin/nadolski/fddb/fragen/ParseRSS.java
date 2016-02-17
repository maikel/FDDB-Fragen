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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class ParseRSS {

   static final String RSS_URL = "http://fddb.info/db/i18n/communityrss/?lang=de";
   static final String DEFAULT_JSON_FILE = "questions.json";

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
            if (old_q.answers().size() < new_q.answers().size()) {
               LinkedList<Answer> diff_answers = new LinkedList<>();
               for (Answer new_a : new_q.answers()) {
                  if (!old_q.answers().contains(new_a)) {
                     diff_answers.add(new_a);
                  }
               }
               diff_questions.add(
                       new Question(old_q.title(), old_q.href(), old_q.author(),
                               diff_answers, old_q.text(), old_q.pub_date()));
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
      List<Question> old_questions = null;
      List<Question> new_questions;
      ObjectMapper mapper = new ObjectMapper();
      // open JSON file
      Path json_path = Paths.get(DEFAULT_JSON_FILE);
      // if file exists open it and load old questions data.
      // TODO: check if path points to a directory and abort the program
      try (BufferedReader json_file = Files.newBufferedReader(json_path)) {
         old_questions = mapper.readValue(json_file, new TypeReference<List<Question>>() {
         });
      } catch (NoSuchFileException exception) {
         System.out.println("No old questions found! Assume first time run.");
      }
      // Read the online rss feed with our parser class and get new list of questions
      Parser parser = new Parser(RSS_URL);
      new_questions = parser.questions();

      // write new questions into json file
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      try (BufferedWriter json_file = Files.newBufferedWriter(json_path)) {
         System.out.println("Writing new questions to data file!");
         mapper.writeValue(json_file, new_questions);
      }
      if (old_questions == null) {
         System.out.println("New questions or answers: ");
         mapper.writeValue(System.out, new_questions);
      } else {
         // show differences in questions and answers
         List<Question> diff_questions = diff_questions(old_questions, new_questions);
         if (diff_questions.size() > 0) {
            System.out.println("New questions or answers: ");
            mapper.writeValue(System.out, diff_questions);
         } else {
            System.out.println("No new questions or answers.");
         }
      }
   }

}
