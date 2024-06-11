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
package sample.mybatis.annotation;

import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import sample.mybatis.annotation.util.RunSingleTask;
import sample.mybatis.annotation.util.RunTask;

@SpringBootApplication
public class SampleAnnotationApplication implements CommandLineRunner {

  public static void main(String[] args) {
    SpringApplication.run(SampleAnnotationApplication.class, args);
  }

  @Autowired
  private SqlSessionTemplate sqlSessionTemplate;

  @Autowired
  private ThreadPoolExecutor threadPoolExecutor;

  @Autowired
  ScheduledExecutorService oneThreadExecutor;

  @Override
  @SuppressWarnings("squid:S106")
  public void run(String... args) {
    System.out.println("SampleAnnotationApplication:" + Arrays.toString(args));
    int exe_size = Integer.parseInt(args[0]);

    if (exe_size == 1) {
      oneThreadExecutor.scheduleAtFixedRate(new RunSingleTask(sqlSessionTemplate), 2, 2, TimeUnit.SECONDS);
    }

    else {
      for (int i = 0; i < exe_size; i++) {
        threadPoolExecutor.execute(new RunTask(sqlSessionTemplate));
      }
      threadPoolExecutor.shutdown();
      long t1 = System.currentTimeMillis();

      while (true) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (threadPoolExecutor.isTerminated()) {
          System.out.println("都结束了！耗时：" + ((System.currentTimeMillis() - t1) / 1000) + "s");
          break;
        }
      }
    }
  }
}
