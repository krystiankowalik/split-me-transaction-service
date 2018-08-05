package com.github.krystiankowalik.splitme.api.transactionsservice.exception.handler

import com.fasterxml.jackson.annotation.JsonFormat
import com.github.krystiankowalik.splitme.api.transactionsservice.util.getCurrentTime
import lombok.Data
import org.springframework.http.HttpStatus

import java.time.LocalDateTime

@Data
class ApiError(val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
               val timestamp: LocalDateTime = getCurrentTime(),
               val message: String = "")