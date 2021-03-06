/**
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.jukito;

import org.jukito.UseModulesTest.Abc;
import org.jukito.UseModulesTest.AbcImpl;
import org.jukito.UseModulesTest.Def;
import org.jukito.UseModulesTest.DefImpl;
import org.jukito.UseModulesTest.Klm;
import org.jukito.UseModulesTest.KlmImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test to check that method injection works fine with external modules.
 */
@RunWith(JukitoRunner.class)
@UseModules({AbcModule.class, DefModule.class})
public class UseModulesTest extends UseModulesTestBase {
    interface Abc {
    }

    interface Def {
    }

    interface Ghj {
    }

    interface Klm {
    }

    static class AbcImpl implements Abc {
    }

    static class DefImpl implements Def {
    }

    static class AbcImpl2 implements Abc {
    }

    static class DefImpl2 implements Def {
    }

    static class KlmImpl implements Klm {
    }

    @Test
    @UseModules(XyzModule.class)
    public void testInjectionUsingMethodModules(Abc abc, Def def) {
        assertTrue(abc instanceof AbcImpl2);
        assertTrue(def instanceof DefImpl2);
    }

    @Test
    public void testInjectionWithExternalModules(Abc abc, Def def, Klm klm) {
        assertTrue(abc instanceof AbcImpl);
        assertTrue(def instanceof DefImpl);
        assertTrue(klm instanceof KlmImpl);
    }

    @Test
    public void testAutoMockingForMissingBindings(Ghj ghj) {
        assertNotNull(ghj);
        assertTrue(Mockito.mockingDetails(ghj).isMock());
    }
}

class XyzModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Abc.class).to(UseModulesTest.AbcImpl2.class);
        bind(Def.class).to(UseModulesTest.DefImpl2.class);
    }
}

@UseModules({DefModule.class, KlmModule.class})
abstract class UseModulesTestBase {
    // KlmModule should get installed
    // DefModule should be ignored because subClass has it
}

class AbcModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Abc.class).to(AbcImpl.class);
    }
}

class DefModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Def.class).to(DefImpl.class);
    }
}

class KlmModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Klm.class).to(KlmImpl.class);
    }
}
