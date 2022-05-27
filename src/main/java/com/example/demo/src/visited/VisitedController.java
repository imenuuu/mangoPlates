package com.example.demo.src.visited;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.ReviewProvider;
import com.example.demo.src.review.ReviewService;
import com.example.demo.src.review.model.PostReviewImgReq;
import com.example.demo.src.review.model.PostReviewListReq;
import com.example.demo.src.review.model.PostReviewReq;
import com.example.demo.src.review.model.ReviewImg;
import com.example.demo.src.visited.model.PostVisitedReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/visited")
public class VisitedController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final VisitedProvider visitedProvider;
    @Autowired
    private final VisitedService visitedService;
    @Autowired
    private final JwtService jwtService;

    public VisitedController(VisitedProvider visitedProvider, VisitedService visitedService, JwtService jwtService) {
        this.visitedProvider = visitedProvider;
        this.visitedService = visitedService;
        this.jwtService = jwtService;
    }

    /**
     * 가봤어요 생성 API
     * [POST] /visited
     * * @return BaseResponse<String>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<String> createVisited(@RequestBody PostVisitedReq postVisitedReq) throws BaseException {
        try {
            Long userIdxByJwt = jwtService.getUserIdx();
            if (postVisitedReq.getUserId() != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            visitedService.createVisited(postVisitedReq);
            String result = "가봤어요 생성 성공";
            return new BaseResponse<>(result);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
