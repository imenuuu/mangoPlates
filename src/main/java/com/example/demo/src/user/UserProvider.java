package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getKakaoLogin(String email) throws BaseException{
        try{
            return userDao.getUserKakaoExists(email);
        }
        catch(Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logInKakao(String k_email) throws BaseException{
        if (userDao.checkEmail(k_email) == 1) {
            Long userIdx = userDao.getIdByEmail(k_email);
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User user;
        try {
            user = userDao.getPwd(postLoginReq);
        } catch (Exception exception){
            throw new BaseException(NON_EXIST_EMAIL);
        }
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            Long userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new PostLoginRes(userId,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public int checkFollowExist(GetUserFollowReq getUserFollowReq) throws BaseException{
        try{
            return userDao.checkFollowExist(getUserFollowReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(Long followedUserId) throws BaseException{
        try{
            return userDao.checkUserExist(followedUserId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkFollowExistToUnFollow(DeleteUserFollowReq deleteUserFollowReq)throws BaseException {
        try{
            return userDao.checkFollowExistToUnFollow(deleteUserFollowReq);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserFollowerListRes> getUserFollower(GetUserFollowListReq getUserFollowReq) throws BaseException{
        if(checkUserExist(getUserFollowReq.getFollowedUserId())==0){
            throw new BaseException(NON_EXIST_USER);
        }
        try{
            List<GetUserFollowerListRes> getUserFollowerListRes =userDao.getUserFollower(getUserFollowReq);
            return getUserFollowerListRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserFollowerListRes> getUserFollowing(GetUserFollowListReq getUserFollowReq)throws BaseException {
        if(checkUserExist(getUserFollowReq.getFollowedUserId())==0){
            throw new BaseException(NON_EXIST_USER);
        }
        try{
            List<GetUserFollowerListRes> getUserFollowerListRes =userDao.getUserFollowing(getUserFollowReq);
            return getUserFollowerListRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserProfileRes> getUserProfile(GetUserProfileReq getUserProfileReq) throws BaseException{
        if(checkUserExist(getUserProfileReq.getProfileUserId())==0){
            throw new BaseException(NON_EXIST_USER);
        }
        try{
            List<GetUserProfileRes> getUserProfileRes=userDao.getUserProfile(getUserProfileReq);
            return getUserProfileRes;
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
