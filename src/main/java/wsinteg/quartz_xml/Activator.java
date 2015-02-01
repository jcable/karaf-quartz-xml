/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wsinteg.quartz_xml;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	XMLScheduler scheduler;

    public void start(BundleContext context) {
        System.out.println("Starting the bundle");
        // fix class loading problem see http://stackoverflow.com/questions/2198928/
        ClassLoader tccl = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
            /*
             * Start threads, or establish connections, here, now
             */
            scheduler = new XMLScheduler();
        } finally {
            Thread.currentThread().setContextClassLoader(tccl);
        }
    }

    public void stop(BundleContext context) {
        System.out.println("Stopping the bundle");
        scheduler.close();
    }

}