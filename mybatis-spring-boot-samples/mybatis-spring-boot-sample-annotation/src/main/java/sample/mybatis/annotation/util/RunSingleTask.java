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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import sample.mybatis.annotation.domain.City;
import sample.mybatis.annotation.mapper.CityMapper;

@Component
public class RunSingleTask implements Runnable {

  private SqlSessionTemplate sqlSessionTemplate;
  private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

  public RunSingleTask(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSessionTemplate = sqlSessionTemplate;
  }

  @Override
  public void run() {
    SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.SIMPLE, true);
    CityMapper cityMapper = session.getMapper(CityMapper.class);

    City city = new City();
    city.setCountry("USA");
    city.setState("CA");
    city.setName(RandomStringUtils.randomAlphabetic(20));
    try {
      cityMapper.insert(city);
      System.out.println(formatter.format(new Date()) + ", " + cityMapper.findByName(city.getName()));
    } catch (Exception e) {
      session.rollback();
    } finally {
      session.close();
    }
  }
}
