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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import sample.mybatis.annotation.domain.City;
import sample.mybatis.annotation.mapper.CityMapper;

@Component
public class RunTask implements Runnable {

  private SqlSessionTemplate sqlSessionTemplate;

  public RunTask(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSessionTemplate = sqlSessionTemplate;
  }

  @Override
  public void run() {
    System.out.println("开始执行插入任务：" + Thread.currentThread().getName());
    long beginTime = System.currentTimeMillis();

    SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
    CityMapper cityMapper = session.getMapper(CityMapper.class);

    City city = new City();
    city.setCountry("USA");
    city.setState("CA");
    int size = 5000;
    try {
      for (int i = 0; i < size; i++) {
        city.setName(RandomStringUtils.randomAlphabetic(20));
        cityMapper.insert(city);
        // System.out.println(cityMapper.findByName(city.getName()));
      }
      session.commit();
      session.clearCache();
    } catch (Exception e) {
      session.rollback();
    } finally {
      session.close();
    }
    System.out.println("thread:" + Thread.currentThread().getName() + "，耗时："
        + ((System.currentTimeMillis() - beginTime) / 1000) + "s");
  }
}
