package com.liao.weatherapp.weatherfactory;

import com.google.gson.annotations.SerializedName;

public class LocateData {

    /**
     * 状态码，0 表示成功
     */
    private int status;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 返回结果
     */
    private ResultBean result;

    // Getter 和 Setter 方法
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    /**
     * 判断请求是否成功
     */
    public boolean isSuccess() {
        return status == 0;
    }

    /**
     * 结果数据类
     */
    public static class ResultBean {
        /**
         * IP地址
         */
        private String ip;

        /**
         * 位置坐标
         */
        private LocationBean location;

        /**
         * 地址信息
         */
        @SerializedName("ad_info")
        private AdInfoBean adInfo;

        // Getter 和 Setter 方法
        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public AdInfoBean getAdInfo() {
            return adInfo;
        }

        public void setAdInfo(AdInfoBean adInfo) {
            this.adInfo = adInfo;
        }
    }

    /**
     * 位置坐标类
     */
    public static class LocationBean {
        /**
         * 纬度
         */
        private double lat;

        /**
         * 经度
         */
        private double lng;

        // Getter 和 Setter 方法
        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    /**
     * 地址信息类
     */
    public static class AdInfoBean {
        /**
         * 国家
         */
        private String nation;

        /**
         * 省份
         */
        private String province;

        /**
         * 城市
         */
        private String city;

        /**
         * 区县
         */
        private String district;

        /**
         * 行政区划代码
         */
        private int adcode;

        // Getter 和 Setter 方法
        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getAdcode() {
            return adcode;
        }

        public void setAdcode(int adcode) {
            this.adcode = adcode;
        }
    }
}