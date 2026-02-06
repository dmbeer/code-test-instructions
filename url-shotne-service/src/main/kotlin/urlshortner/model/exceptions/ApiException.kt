package com.example.urlshortner.model.exceptions

class ValidationException(override val message: String, override val cause: Throwable?): Throwable()
class ParsingException(override val message: String, override val cause: Throwable?): Throwable()