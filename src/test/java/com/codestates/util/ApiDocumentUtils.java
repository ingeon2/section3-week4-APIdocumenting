package com.codestates.util;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

public interface ApiDocumentUtils {
    static OperationRequestPreprocessor getRequestPreProcessor() {
        return preprocessRequest(prettyPrint());
    }

    static OperationResponsePreprocessor getResponsePreProcessor() {
        return preprocessResponse(prettyPrint());
    }
}

//preprocessRequest(prettyPrint()) 는 문서에 표시되는 JSON 포맷의 request body를 예쁘게 표현해 줍니다.
//preprocessResponse(prettyPrint()) 는 문서에 표시되는 JSON 포맷의 response body를 예쁘게 표현해 줍니다.