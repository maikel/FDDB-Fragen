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
public class QuestionTest {
    Question question;
    
    public QuestionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        question = new Question("foo", "bar", "maikel", 2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of title method, of class Question.
     */
    @Test
    public void testTitle() {;
        String expResult = "foo";
        String result = question.title();
        assertEquals(expResult, result);
    }

    /**
     * Test of href method, of class Question.
     */
    @Test
    public void testHref() {
        String expResult = "bar";
        String result = question.href();
        assertEquals(expResult, result);
    }

    /**
     * Test of author method, of class Question.
     */
    @Test
    public void testAuthor() {
        String expResult = "maikel";
        String result = question.author();
        assertEquals(expResult, result);
    }

    /**
     * Test of num_answers method, of class Question.
     */
    @Test
    public void testNum_answers() {
        int expResult = 2;
        int result = question.num_answers();
        assertEquals(expResult, result);
    }
    
}
