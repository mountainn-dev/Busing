package com.san.busing.data.exception

import java.io.IOException

object ServiceException {
    class NoResultException(override val message: String?) : IOException(message)
}