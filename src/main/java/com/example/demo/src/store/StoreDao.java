package com.example.demo.src.store;


import com.example.demo.config.BaseException;
import com.example.demo.src.store.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Repository
public class StoreDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public List<GetStoreListRes> getStoreList(GetStoreListReq getStoreListReq) {
        String inSql=String.join(",",getStoreListReq.getRegion().stream().map(region -> "'"+region+"'").collect(Collectors.toList()));
        String getStoreListQuery = String.format("SELECT Stores.id as 'storeId',(select ReviewImgSelect.imgurl from ReviewImg ReviewImgSelect\n" +
                "        left join Review on Review.id=reviewId where ReviewImgSelect.reviewId=Review.id and Stores.id=Review.storeId limit 1)as 'reviewImg',\n" +
                "       concat(ROUND((6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "                      *cos(radians(Stores.longitude) -radians(Users.longitude))\n" +
                "                      +sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))),3),'km')\n" +
                "    AS distance,concat(Stores.name)'storeName',Stores.foodCategory,rating,viewCount,\n" +
                "        (select count(Review.id) from Review where Review.storeId=Stores.id limit 1)'reviewCount'\n" +
                "FROM Users,Stores\n" +
                "where Users.id=? and Stores.subRegion IN (%s) LIMIT ?,10",inSql);
        Object[] getStoreListParams=new Object[]{
                getStoreListReq.getUserId(),(getStoreListReq.getPage()-1)*10
        };
        return this.jdbcTemplate.query(getStoreListQuery,
                (rs,rowNum)-> new GetStoreListRes(
                        rs.getLong("storeId"),
                        rs.getString("reviewImg"),
                        rs.getString("distance"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("viewCount"),
                        rs.getInt("reviewCount")
                ),getStoreListParams);
    }

    public GetStoreRes getStore(Long storeId){
        String getStoreQuery = "SELECT DISTINCT S.*, COUNT(R.id) AS reviewCount, COUNT(W.id) AS wishCount FROM Stores S\n" +
                "LEFT JOIN Review R on S.id = R.storeId\n" +
                "LEFT JOIN Wishes W ON S.id = W.storeId\n" +
                "WHERE S.id = ?";
        Long getStoreParam = storeId;
        return this.jdbcTemplate.queryForObject(getStoreQuery,
                (rs, rowNum) -> new GetStoreRes(
                        rs.getString("name"),
                        rs.getLong("viewCount"),
                        rs.getLong("reviewCount"),
                        rs.getLong("wishCount"),
                        rs.getFloat("rating"),
                        rs.getString("address"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"),
                        rs.getString("telephone"),
                        rs.getString("openTime"),
                        rs.getString("breakTime"),
                        rs.getString("dayOff"),
                        rs.getString("priceInfo"),
                        rs.getString("foodCategory"),
                        rs.getString("parkingInfo"),
                        rs.getString("website"),
                        rs.getLong("creatorId"),
                        rs.getString("updatedAt")),
                getStoreParam);
    }

    public void increaseViewCount(Long storeId){
        String increaseQuery = "UPDATE Stores SET viewCount = viewCount + 1 WHERE id = ?";
        Long increaseParam = storeId;
        this.jdbcTemplate.update(increaseQuery, increaseParam);
    }

    public GetMenuRes getMenu(Long storeId){
        String getMenuListQuery = "SELECT * FROM Menu WHERE storeId = ?";
        String getImgListQuery = "SELECT imgUrl FROM MenuImg WHERE storeId = ?";
        Long Param = storeId;
        List<GetMenuDetailRes> getMenuDetailRes;
        List<GetMenuImgRes> getMenuImgRes;

        return new GetMenuRes(
                getMenuDetailRes = this.jdbcTemplate.query(getMenuListQuery,
                        (rs,rowNum) -> new GetMenuDetailRes(
                                rs.getString("name"),
                                rs.getInt("price"),
                                rs.getString("updatedAt")), Param),
                getMenuImgRes = this.jdbcTemplate.query(getImgListQuery,
                        (rs,rowNum) -> new GetMenuImgRes(
                                rs.getString("imgUrl")), Param)
        );
    }

    public List<GetStoreListRes> getStoreListByKeyWord(GetStoreListByKeyWordReq getStoreListByKeyWordReq) {
        String inSql=String.join(",",getStoreListByKeyWordReq.getRegion().stream().map(region -> "'"+region+"'").collect(Collectors.toList()));
        String getStoreListQuery = String.format("SELECT Stores.id as 'storeId',(select ReviewImgSelect.imgurl from ReviewImg ReviewImgSelect\n" +
                "        left join Review on Review.id=reviewId where ReviewImgSelect.reviewId=Review.id and Stores.id=Review.storeId limit 1)as 'reviewImg',\n" +
                "       concat(ROUND((6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "                      *cos(radians(Stores.longitude) -radians(Users.longitude))\n" +
                "                      +sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))),3),'km')\n" +
                "    AS distance,concat(Stores.name)'storeName',Stores.foodCategory,rating,viewCount,\n" +
                "        (select count(Review.id) from Review where Review.storeId=Stores.id limit 1)'reviewCount'\n" +
                "FROM Users,Stores\n" +
                "where Users.id=? and Stores.subRegion IN (%s) and Stores.name like ? LIMIT ?,10 ",inSql);
        String keyword="%"+getStoreListByKeyWordReq.getKeyword()+"%";
        Object[] getStoreListParams=new Object[]{
                getStoreListByKeyWordReq.getUserId(),keyword,(getStoreListByKeyWordReq.getPage()-1)*10
        };
        return this.jdbcTemplate.query(getStoreListQuery,
                (rs,rowNum)-> new GetStoreListRes(
                        rs.getLong("storeId"),
                        rs.getString("reviewImg"),
                        rs.getString("distance"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("viewCount"),
                        rs.getInt("reviewCount")
                ),getStoreListParams);
    }

    public int checkStoreId(Long storeId){
        String checkStoreIdQuery = "select exists(select id from Stores where id = ?)";
        Long checkStoreIdParams = storeId;
        return this.jdbcTemplate.queryForObject(checkStoreIdQuery,
                int.class,
                checkStoreIdParams);
    }

    public List<GetStoreListRes> getStoreListByFood(GetStoreListByFoodReq getStoreListByFoodReq) {
        String categoryList=String.join(",",getStoreListByFoodReq.getCategory().stream().map(category -> "'"+category+"'").collect(Collectors.toList()));
        String regionList=String.join(",",getStoreListByFoodReq.getRegion().stream().map(region -> "'"+region+"'").collect(Collectors.toList()));

        String getStoreListQuery = String.format("SELECT Stores.id AS storeId, (SELECT RI.imgurl FROM ReviewImg RI, Review R WHERE RI.reviewId=R.id limit 1) AS reviewImg,\n" +
                "       CONCAT(subRegion,' ',ROUND((6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "           *cos(radians(Stores.longitude) -radians(Users.longitude))+sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))),3),'km') AS distance,\n" +
                "       Stores.name AS storeName, rating, viewCount, (SELECT count(Review.id) FROM Review WHERE Review.storeId=Stores.id) AS reviewCount\n" +
                "FROM Users,Stores\n" +
                "WHERE Users.id=? && Stores.foodCategory IN (%s) && Stores.subRegion IN (%s) \n" +
                "LIMIT ?,10",categoryList, regionList);
        Object[] getStoreListByFoodParams=new Object[]{
                getStoreListByFoodReq.getUserId(),(getStoreListByFoodReq.getPage()-1)*10
        };
        return this.jdbcTemplate.query(getStoreListQuery,
                (rs,rowNum)-> new GetStoreListRes(
                        rs.getLong("storeId"),
                        rs.getString("reviewImg"),
                        rs.getString("distance"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("viewCount"),
                        rs.getInt("reviewCount")
                ),getStoreListByFoodParams);
    }

    public List<GetStoreListRes> getStoreListByParking(Long userId, List<String> region, int page) {
        String regionList=String.join(",",region.stream().map(region_ -> "'"+region_+"'").collect(Collectors.toList()));
        String getStoreListByParkingQuery = String.format("SELECT Stores.id AS storeId, (SELECT RI.imgurl FROM ReviewImg RI, Review R WHERE RI.reviewId=R.id limit 1) AS reviewImg,\n" +
                "       CONCAT(subRegion,' ',ROUND((6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "           *cos(radians(Stores.longitude) -radians(Users.longitude))+sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))),3),'km') AS distance,\n" +
                "       Stores.name AS storeName, rating, viewCount, (SELECT count(Review.id) FROM Review WHERE Review.storeId=Stores.id) AS reviewCount\n" +
                "FROM Users,Stores\n" +
                "WHERE Users.id=? && Stores.parkingInfo = '가능' && Stores.subRegion IN (%s)\n" +
                "LIMIT ?,10", regionList);
        Object[] getStoreListByParkingParams = new Object[] {userId, (page - 1) * 10};
        return this.jdbcTemplate.query(getStoreListByParkingQuery,
                (rs,rowNum)-> new GetStoreListRes(
                        rs.getLong("storeId"),
                        rs.getString("reviewImg"),
                        rs.getString("distance"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("viewCount"),
                        rs.getInt("reviewCount")
                ),getStoreListByParkingParams);
    }


    public List<GetStoreListRes> getStoreListByDistance(Long userId, int distance, int page) {
        String getStoreListByDistanceQuery = "SELECT Stores.id AS storeId, (SELECT RI.imgurl FROM ReviewImg RI, Review R WHERE RI.reviewId=R.id limit 1) AS reviewImg,\n" +
                "       CONCAT(subRegion,' ',ROUND((6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "           *cos(radians(Stores.longitude) -radians(Users.longitude))+sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))),3),'km') AS distance,\n" +
                "       Stores.name AS storeName, rating, viewCount, (SELECT count(Review.id) FROM Review WHERE Review.storeId=Stores.id) AS reviewCount\n" +
                "FROM Users,Stores\n" +
                "WHERE Users.id=? && (6371*acos(cos(radians(Users.Latitude))*cos(radians(Stores.Latitude))\n" +
                "*cos(radians(Stores.longitude) -radians(Users.longitude))+sin(radians(Users.Latitude))*sin(radians(Stores.Latitude)))) <= ?\n" +
                "LIMIT ?,10";
        Object[] getStoreListParams=new Object[]{userId, distance * 0.001, (page-1)*10};
        return this.jdbcTemplate.query(getStoreListByDistanceQuery,
                (rs,rowNum)-> new GetStoreListRes(
                        rs.getLong("storeId"),
                        rs.getString("reviewImg"),
                        rs.getString("distance"),
                        rs.getString("storeName"),
                        rs.getFloat("rating"),
                        rs.getInt("viewCount"),
                        rs.getInt("reviewCount")
                ),getStoreListParams);
    }
}

