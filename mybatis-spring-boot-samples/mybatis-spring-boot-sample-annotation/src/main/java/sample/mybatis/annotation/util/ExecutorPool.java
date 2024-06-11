/*
 *    Copyright 2015-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package sample.mybatis.annotation.util;

import java.util.concurrent.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutorPool {
  @Bean
  public ThreadPoolExecutor threadPoolExecutor() {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 80, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    executor.allowCoreThreadTimeOut(true);
    return executor;
  }

  @Bean
  public ScheduledExecutorService oneThreadExecutor() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
    return executor;
  }
}
