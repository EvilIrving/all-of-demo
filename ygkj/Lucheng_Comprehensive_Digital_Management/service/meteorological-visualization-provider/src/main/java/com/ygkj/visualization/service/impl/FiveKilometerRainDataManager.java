package com.ygkj.visualization.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;

//@Component
public class FiveKilometerRainDataManager {

    private RedisTemplate redisTemplate;

    @Value("${weather.syn-five-km-rain-path}")
    private String path;

    private float[][] rainData;

    private float[] longitude;

    private float[] latitude;

    private JSONArray array;

    private static final String FILE_KEY = "5km_rain_data_file";

    private Date updateTime;

    private DelayQueue<UpdateRainDataTask> queue = new DelayQueue();

    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public FiveKilometerRainDataManager(@Autowired RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init(){
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
        File[] files = file.listFiles();
        if (files.length == 1){
            String absolutePath = files[0].getAbsolutePath();
            handleFile(absolutePath);
        }
        queue.offer(new UpdateRainDataTask(TimeUnit.MINUTES.toMillis(5l)));
        new Thread(() -> {
            while (true){
                try {
                    UpdateRainDataTask task = queue.take();
                    executor.submit(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handleFile(String filePath){
        if (!filePath.equals(redisTemplate.opsForValue().get(FILE_KEY)) || updateTime == null){
            try(NetcdfFile ncfile = NetcdfDataset.open(filePath)) {
                // 存经纬度
                String var1 = "lon";// 此处严格区分大小写，不然找不到，不知道有什么变量的可以断点debug一下，鼠标移到上面ncfile那行看
                String var2 = "lat";
                String pr01 = "data";
                Variable v1 = ncfile.findVariable(var1);
                Variable v2 = ncfile.findVariable(var2);
                Variable v3 = ncfile.findVariable(pr01);
                longitude = (float[]) v1.read().copyToNDJavaArray();// 因为经纬度是二维的，直接
                // copyToNDJavaArray结果就是二维的，强转一下就好，然后java的二维数组大家该咋用咋用就行了
                latitude = (float[]) v2.read().copyToNDJavaArray();
                rainData = (float[][]) v3.read().copyToNDJavaArray();
                array = new JSONArray(latitude.length*longitude.length);
                for (int i = 0; i < latitude.length; i++) {
                    for (int j = 0; j < longitude.length; j++) {
                        JSONObject object = new JSONObject();
                        object.put("latitude",latitude[i]);
                        object.put("longitude",longitude[j]);
                        object.put("data",rainData[i][j]);
                        array.add(object);
                    }
                }
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            } finally {
                redisTemplate.opsForValue().set(FILE_KEY,filePath);
                updateTime = new Date();
            }
        }
    }

    class UpdateRainDataTask implements Runnable, Delayed {
        private long now;

        private long endTimestamp;

        public UpdateRainDataTask(long time) {
            this.now = System.currentTimeMillis();
            this.endTimestamp = now + time;
        }

        @Override
        public void run() {
            try {
                File file = new File(path);
                File[] files = file.listFiles();
                if (files.length == 1){
                    handleFile(files[0].getAbsolutePath());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                queue.add(new UpdateRainDataTask(TimeUnit.MINUTES.toMillis(5l)));
            }
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(endTimestamp - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }
    }

    public JSONArray get5kmCorrectData(){
        return array;
    }

    public Date getUpdateTime(){
        return updateTime;
    }


}
