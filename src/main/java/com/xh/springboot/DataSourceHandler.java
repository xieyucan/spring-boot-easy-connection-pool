package com.xh.springboot;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by xiehui1956(@)gmail.com on 2020/6/9
 */
public interface DataSourceHandler {

    Map<String, DataSource> getCustomDataSources();

}
