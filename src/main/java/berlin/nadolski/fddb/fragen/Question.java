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

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;

/**
 * This class is a data model representing a single question from FDDB.
 * 
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class Question {
    public Question(
            @JsonProperty("title") String title,
            @JsonProperty("href") String href,
            @JsonProperty("author") String author,
            @JsonProperty("answers") List<Answer> answers,
            @JsonProperty("text") String text,
            @JsonProperty("date") Date pub_date) {
        m_title = title;
        m_href = href;
        m_author = author;
        m_answers = answers;
        m_text = text;
        m_pub_date = pub_date;
    }
    
    private final String m_title;
    @JsonGetter(value = "title")
    public String getTitle() { return m_title; }
    
    private final String m_href;
    @JsonGetter(value = "href")
    public String getHref() { return m_href; }
    
    private final String m_author;
    @JsonGetter(value = "author")
    public String getAuthor() { return m_author; }
    
    private final Date m_pub_date;
    @JsonGetter(value = "date")
    public Date getDate() { return m_pub_date; }
    
    private final String m_text;
    @JsonGetter(value = "text")
    public String getText() { return m_text; }
    
    private final List<Answer> m_answers;
    @JsonGetter(value = "answers")
    public List<Answer> getAnswers() { return m_answers; }
    
    @Override
    public boolean equals(Object other) {
       boolean result = false;
       if (other != null && other instanceof Question) {
          result = 0 == this.getHref().compareTo(((Question)other).getHref());
       }
       return result;
    }
}
