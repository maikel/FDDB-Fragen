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

import java.util.Date;
import java.util.List;

/**
 * This class is a data model representing a single question from FDDB.
 * 
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class Question {
    public Question(String title, String href, String author, List<Answer> answers, String text, Date pub_date) {
        m_title = title;
        m_href = href;
        m_author = author;
        m_answers = answers;
        m_text = text;
        m_pub_date = pub_date;
    }
    
    private final String m_title;
    public String title() { return m_title; }
    
    private final String m_href;
    public String href() { return m_href; }
    
    private final String m_author;
    public String author() { return m_author; }
    
    private final List<Answer> m_answers;
    public List<Answer> answers() { return m_answers; }
    
    private final Date m_pub_date;
    public Date pub_date() { return m_pub_date; }
    
    private final String m_text;
    public String text() { return m_text; }
    
    @Override
    public String toString() {
        String json = "{";
        json += "title:\"" + m_title + "\"";
        json += ",author:\"" + m_author + "\"";
        json += ",href:\"" + m_href + "\"";
        json += ",answers:" + m_answers;
        json += "}";
        return json;
    }
}
