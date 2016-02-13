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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Maikel Nadolski <maikel@nadolski.berlin>
 */
public class ParserTest {
    
    Parser m_parser;
    
    public ParserTest() throws IOException {
//        m_parser = new Parser("Fragen - Fddb.html");
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
//    @Test
//    public void constrctorNull() throws IOException {
//        Parser parser = new Parser(null);
//        assert(parser != null);
//    }

    @Test
    public void emptyString() throws IOException {
        try {
            Parser parser = new Parser("");
            fail("No Assertion Error for empty Document!");
        } catch (AssertionError e) {

        }
    }
    
    @Test
    public void noElements() throws IOException {
        try {
            Parser parser = new Parser("<html></html>");
            fail("No Assertion Error for no div.standardcontent Document!");
        } catch (AssertionError e) {

        }
    }
    
    @Test
    public void numberOfQuestions() throws IOException {        
        assert(!m_parser.questions().isEmpty());
        assert(m_parser.questions().size() == 17);
    }
    
    @Test
    public void contentOfQuestions() throws IOException {
        assert(m_parser.questions().get(0).author().compareTo("Strazulla") == 0);
    }
    
    @Test 
    public void internetTest() throws IOException {
        Parser parser = new Parser("http://fddb.info/db/i18n/communityrss");
        assert(!m_parser.questions().isEmpty());
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
