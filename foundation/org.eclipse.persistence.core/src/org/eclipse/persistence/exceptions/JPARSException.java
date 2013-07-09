/*******************************************************************************
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *      gonural - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.exceptions;

import org.eclipse.persistence.exceptions.i18n.ExceptionMessageGenerator;

public class JPARSException extends EclipseLinkException {
    // Next range should start from LAST_ERROR_CODE (62000). 
    // The JPA-RS uses error codes between 61000-61999 (both inclusive).
    public enum ErrorCode {
        ENTITY_NOT_FOUND(61000),
        OBJECT_REFERRED_BY_LINK_DOES_NOT_EXIST(61001),
        INVALID_CONFIGURATION(61002),
        ENTITY_NOT_IDEMPOTENT(61003),
        PERSISTENCE_CONTEXT_COULD_NOT_BE_BOOTSTRAPPED(61004),
        CLASS_DESCRIPTOR_COULD_NOT_BE_FOUND_FOR_ENTITY(61005),
        DATABASE_MAPPING_COULD_NOT_BE_FOUND_FOR_ENTITY_ATTRIBUTE(61006),
        ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY(61007),
        SELECTION_QUERY_FOR_ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY(61008),
        INVALID_PAGING_REQUEST(61009),
        ATTRIBUTE_COULD_NOT_BE_UPDATED(61010),
        INVALID_ATTRIBUTE_REMOVAL_REQUEST(61011),
        RESPONSE_COULD_NOT_BE_BUILT_FOR_FIND_ATTRIBUTE_REQUEST(61012),
        SESSION_BEAN_LOOKUP_FAILED(61013),
        RESPONSE_COULD_NOT_BE_BUILT_FOR_NAMED_QUERY_REQUEST(61014),

        INVALID_SERVICE_VERSION(61013),

        // wraps eclipselink exceptions    
        AN_EXCEPTION_OCCURRED(61999),

        // end marker for JPA-RS error codes    
        LAST_ERROR_CODE(62000);
        private int value;

        private ErrorCode(int value) {
            this.value = value;
        }

        /**
         * Value.
         *
         * @return the string
         */
        public String value() {
            return String.valueOf(value);
        }
    };

    private int httpStatusCode;

    /**
     * Instantiates a new JPARS exception.
     */
    public JPARSException() {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.persistence.exceptions.EclipseLinkException#getMessage()
     */
    @Override
    public String getMessage() {
        return super.getUnformattedMessage();
    }

    /**
     * Gets the http status code.
     *
     * @return the http status code
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * Sets the http status code.
     *
     * @param httpStatusCode the new http status code
     */
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    private JPARSException(String message) {
        super(message);
    }

    private JPARSException(Throwable internalException) {
        super(ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.AN_EXCEPTION_OCCURRED.value, new Object[] {}), internalException);
    }

    private JPARSException(String msg, Throwable internalException) {
        super(msg, internalException);
    }

    /**
     * Entity not found.
     *
     * @param httpStatusCode the http status code
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException entityNotFound(int httpStatusCode, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.ENTITY_NOT_FOUND.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.ENTITY_NOT_FOUND.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Class descriptor could not be found for entity.
     *
     * @param httpStatusCode the http status code
     * @param entityType the entity type
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException classDescriptorCouldNotBeFoundForEntity(int httpStatusCode, String entityType, String persistenceUnit) {
        Object[] args = { entityType, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.CLASS_DESCRIPTOR_COULD_NOT_BE_FOUND_FOR_ENTITY.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.CLASS_DESCRIPTOR_COULD_NOT_BE_FOUND_FOR_ENTITY.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Attribute could not be found for entity.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException attributeCouldNotBeFoundForEntity(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { attributeName, entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Selection query for attribute could not be found for entity.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException selectionQueryForAttributeCouldNotBeFoundForEntity(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { attributeName, entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.SELECTION_QUERY_FOR_ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.SELECTION_QUERY_FOR_ATTRIBUTE_COULD_NOT_BE_FOUND_FOR_ENTITY.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Invalid paging request.
     *
     * @param httpStatusCode the http status code
     * @return the JPARS exception
     */
    public static JPARSException invalidPagingRequest(int httpStatusCode) {
        Object[] args = {};

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.INVALID_PAGING_REQUEST.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.INVALID_PAGING_REQUEST.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Database mapping could not be found for entity attribute.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException databaseMappingCouldNotBeFoundForEntityAttribute(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { attributeName, entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.DATABASE_MAPPING_COULD_NOT_BE_FOUND_FOR_ENTITY_ATTRIBUTE.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.DATABASE_MAPPING_COULD_NOT_BE_FOUND_FOR_ENTITY_ATTRIBUTE.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Attribute could not be updated.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException attributeCouldNotBeUpdated(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { attributeName, entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.ATTRIBUTE_COULD_NOT_BE_UPDATED.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.ATTRIBUTE_COULD_NOT_BE_UPDATED.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Invalid service version.
     *
     * @param httpStatusCode the http status code
     * @param serviceVersion the service version
     * @return the JPARS exception
     */
    public static JPARSException invalidServiceVersion(int httpStatusCode, String serviceVersion) {
        Object[] args = { serviceVersion };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.INVALID_SERVICE_VERSION.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.INVALID_SERVICE_VERSION.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Invalid remove attribute request.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException invalidRemoveAttributeRequest(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.INVALID_ATTRIBUTE_REMOVAL_REQUEST.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.INVALID_ATTRIBUTE_REMOVAL_REQUEST.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Response could not be built for find attribute request.
     *
     * @param httpStatusCode the http status code
     * @param attributeName the attribute name
     * @param entityType the entity type
     * @param entityId the entity id
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException responseCouldNotBeBuiltForFindAttributeRequest(int httpStatusCode, String attributeName, String entityType, String entityId, String persistenceUnit) {
        Object[] args = { entityType, entityId, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.RESPONSE_COULD_NOT_BE_BUILT_FOR_FIND_ATTRIBUTE_REQUEST.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.RESPONSE_COULD_NOT_BE_BUILT_FOR_FIND_ATTRIBUTE_REQUEST.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Response could not be built for named query request.
     *
     * @param httpStatusCode the http status code
     * @param query the query
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException responseCouldNotBeBuiltForNamedQueryRequest(int httpStatusCode, String query, String persistenceUnit) {
        Object[] args = { query, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.RESPONSE_COULD_NOT_BE_BUILT_FOR_NAMED_QUERY_REQUEST.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.RESPONSE_COULD_NOT_BE_BUILT_FOR_NAMED_QUERY_REQUEST.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Object referred by link does not exist.
     *
     * @param httpStatusCode the http status code
     * @param entityType the entity type
     * @param entityId the entity id
     * @return the JPARS exception
     */
    public static JPARSException objectReferredByLinkDoesNotExist(int httpStatusCode, String entityType, Object entityId) {
        Object[] args = { entityType, entityId };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.OBJECT_REFERRED_BY_LINK_DOES_NOT_EXIST.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.OBJECT_REFERRED_BY_LINK_DOES_NOT_EXIST.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Session bean lookup failed.
     *
     * @param httpStatusCode the http status code
     * @param jndiName the jndi name
     * @return the JPARS exception
     */
    public static JPARSException sessionBeanLookupFailed(int httpStatusCode, String jndiName) {
        Object[] args = { jndiName };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.SESSION_BEAN_LOOKUP_FAILED.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.SESSION_BEAN_LOOKUP_FAILED.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Invalid configuration.
     *
     * @param httpStatusCode the http status code
     * @return the JPARS exception
     */
    public static JPARSException invalidConfiguration(int httpStatusCode) {
        Object[] args = {};

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.INVALID_CONFIGURATION.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.INVALID_CONFIGURATION.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Entity is not idempotent.
     *
     * @param httpStatusCode the http status code
     * @param entityType the entity type
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException entityIsNotIdempotent(int httpStatusCode, String entityType, String persistenceUnit) {
        Object[] args = { entityType, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.ENTITY_NOT_IDEMPOTENT.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.ENTITY_NOT_IDEMPOTENT.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Persistence context could not be bootstrapped.
     *
     * @param httpStatusCode the http status code
     * @param persistenceUnit the persistence unit
     * @return the JPARS exception
     */
    public static JPARSException persistenceContextCouldNotBeBootstrapped(int httpStatusCode, String persistenceUnit) {
        Object[] args = { persistenceUnit, persistenceUnit };

        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.PERSISTENCE_CONTEXT_COULD_NOT_BE_BOOTSTRAPPED.value, args);
        JPARSException exception = new JPARSException(msg);
        exception.setErrorCode(ErrorCode.PERSISTENCE_CONTEXT_COULD_NOT_BE_BOOTSTRAPPED.value);
        exception.setHttpStatusCode(httpStatusCode);

        return exception;
    }

    /**
     * Exception occurred.
     *
     * @param exception the exception
     * @return the JPARS exception
     */
    public static JPARSException exceptionOccurred(Exception exception) {
        int errorCode = ErrorCode.AN_EXCEPTION_OCCURRED.value;
        String msg = ExceptionMessageGenerator.buildMessage(JPARSException.class, ErrorCode.AN_EXCEPTION_OCCURRED.value, new Object[] { exception.getClass().getSimpleName() }).trim();

        if (exception instanceof EclipseLinkException) {
            errorCode = ((EclipseLinkException) exception).getErrorCode();
            msg = ((EclipseLinkException) exception).getClass().getName().trim();
        } else if (exception.getCause() instanceof EclipseLinkException) {
            errorCode = ((EclipseLinkException) (exception.getCause())).getErrorCode();
            msg = ((EclipseLinkException) (exception.getCause())).getClass().getName().trim();
        }

        JPARSException jparsException = new JPARSException(msg, exception);
        jparsException.setErrorCode(errorCode);
        jparsException.setInternalException(exception);

        return jparsException;
    }
}