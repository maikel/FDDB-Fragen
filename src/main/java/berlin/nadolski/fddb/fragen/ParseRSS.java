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

import java.io.IOException;

/**
 *
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class ParseRSS {
    
    static final String RSS_URL = "http://fddb.info/db/i18n/communityrss/?lang=de";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(RSS_URL);
        parser.questions().stream().forEach((q) -> {
            System.out.println("Title: " + q.title());
            System.out.println("Created: " + q.pub_date() + "\n");
        });
    }
    
}
