package com.resultmanagement.student.service;

import com.resultmanagement.student.controller.ResultAddRequest;
import com.resultmanagement.student.controller.ResultResponse;
import com.resultmanagement.student.repo.Result;
import org.mapstruct.Mapper;

@Mapper
public interface ResultMapper {
    Result toResult(ResultAddRequest request);

    ResultResponse toResultResponse(Result result);
}
