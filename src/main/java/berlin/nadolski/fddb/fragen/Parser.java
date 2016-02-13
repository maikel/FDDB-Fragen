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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * Use JSoup to get and interpret HTML source code as an DOM object and fetch
 * all necessary informations for the reader.
 * 
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class Parser {
    /**
     * Interpret the source string either as an URL or a file name or a HTML
     * string.
     * 
     * First check if `source` is a valid URL, if not try to open a file named
     * as the parameter and if that also doesnt work, take the string as HTML
     * source code itself.
     * 
     * @param source - an URL or a file name or HTML source code.
     * @throws java.io.IOException
     */
    public Parser(String source) throws IOException {  
        logger.trace("Entering constructor");
        m_dom = create_dom_from_source(source);
        logger.trace("Created Document Model. m_dom.toString() = ");
        logger.trace(m_dom.toString());
        assert(m_dom != null);
        assert(!m_dom.data().isEmpty());
        m_questions = create_questions_from_dom(m_dom);
        assert(m_questions != null);
        logger.trace("Leaving constructor");
    }
    
    /**
     * Parse DOM object and interpret the given argument in possibly multiple
     * ways. Used to initialize the DOM object in the contructor. The argument
     * stirng can either be an URL or a file name or XML code in a string itself.
     * 
     * @todo need to get encoding working...
     * @param source
     * @return a jsoup DOM object
     * @throws IOException 
     */
    private static Document create_dom_from_source(String source) throws IOException {
        logger.trace("Entering create_dom_from_source()");
        Document dom;
        try {
            logger.debug("Trying if given argument `source` is an URL.");
            URL url = new URL(source);
            logger.debug("Success. Fetching HTML code from the internet...");
            dom = Jsoup.connect(source).get();
//            dom = Jsoup.parse(url.openStream(), "ISO-8859-1", source);
        } catch (MalformedURLException e) {
            logger.debug("Failed. Try to open a file named after argument `source`...");
            File file = new File(source);
            if (file.exists() && !file.isDirectory()) {
                logger.debug("Success. Opening, reading and parsing File...");
                String html = new String(Files.readAllBytes(file.toPath()));
                dom = Jsoup.parse(html);
            } else {
                logger.debug("Failed. Parse argument `source` as HTML code itself.");
                dom = Jsoup.parse(source);
            }
        }
        logger.trace("Leaving create_dom_from_source()");
        return dom;
    }
    
    /**
     * Parse the RSS file of FDDB and recursivle call get_answers on the
     * question page URLs.
     * 
     * @param dom
     * @return a list of question model objects
     */
    private static List<Question> create_questions_from_dom(Document dom) throws IOException {
        logger.trace("Entering create_questions_from_dom()");
        // the div named by this class contains all questions in seperate divs
        Elements question_items = dom.select("item");
        assert(question_items != null);

        logger.debug("Number of questions in this DOM is: "
                + question_items.size());
        logger.debug("Creating models from DOM Elements...");
        LinkedList<Question> question_list = new LinkedList<>();
        for (Element question : question_items) {
            try {
                logger.trace("\nquestion source code:\n" + question.toString());
                String title = question.select("title").text();
                String href = question.select("link").text();
                String author = question.select("author").text();
                String description = question.select("description").text();
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss ZZZZZ", Locale.ENGLISH);
                Date date = format.parse(question.select("pubDate").text());
                List<Answer> answers = get_answers_from_url(href);
                question_list.add(new Question(title, href, author, answers, description, date));
                logger.debug("Created question:" + question_list.getLast().toString());
            } catch (ParseException ex) {
                logger.fatal(ex.getMessage());
            }
        }
        logger.trace("Leaving create_questions_from_dom()");
        return question_list;
    }
    
    /**
     * Connect to url `href` and parse the HTML code for answers.
     * 
     * @param href
     * @return
     * @throws IOException 
     */
    private static List<Answer> get_answers_from_url(String href) throws IOException {
        Document dom = Jsoup.connect(href).get();
        logger.trace(dom.toString());
        Element div_standardcontent = dom.select("div.standardcontent").first();
        Elements answer_tables = div_standardcontent.select("table");
        LinkedList<Answer> answers = new LinkedList<>();
        for (Element answer_table : answer_tables) {
           logger.trace(answer_table.toString());
           answers.add(null);
        }
        return answers;
    }
    
    /**
     * Getter
     * @return a copy of the list of questions
     */
    public List<Question> questions() {
        return new LinkedList<>(m_questions);
    }
    
    private final Document m_dom;
    private final List<Question> m_questions;
    private final static Logger logger = LogManager.getLogger(Parser.class);
}
