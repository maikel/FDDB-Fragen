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

/**
 * The model to represent Answers to a Question.
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class Answer {
    public Answer(@JsonProperty("author") String author,
                   @JsonProperty("text") String text) {
        m_author = author;
        m_text   = text;
    } 
    
    final String m_author;
    @JsonGetter(value = "author")
    public String author() { return m_author; }
    final String m_text;
    @JsonGetter(value = "text")
    public String text() { return m_text; }
    
    @Override
    public boolean equals(Object other) {
       boolean result = false;
       if (other != null && other instanceof Answer) {
          result = 0 == this.text().compareTo(((Answer)other).text());
          result = result && 0 == this.author().compareTo(((Answer)other).author());
       }
       return result;
    }
}
