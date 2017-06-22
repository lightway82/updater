package org.anantacreative.updater.tests;

import java.util.Optional;

/**
 * Simple result container for tests
 * @param <T>
 */
public class Value<T> {
    private T value;
    private boolean present;
    private boolean error;
    private boolean complete;
    private Exception exception;

    public Value() {
        present = false;
        error = false;
        complete = false;
    }

    /**
     * Установлено значение, ошибки небыло!!!
     * @return
     */
    public boolean isPresent() {
        return present && !error && complete;
    }



    /**
     * Была выставлена ошибка
     * @return
     */
    public boolean isError() {
        return error;
    }

    /**
     * Завершено, когда выставлена ошибка или результат
     * @return
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * Устанавливает ошибку.
     * @param e
     */
    public void setError(Exception e) {
        error = true;
        complete = true;
        exception =e;

    }
    /**
     * Устанавливает ошибку.
     *
     */
    public void setError() {
        error = true;
        complete = true;
        exception =null;

    }

    /**
     * Возврат ошибки.
     * @return
     */
    public Optional<Exception> getErrorCause(){return Optional.of(exception);}

    /**
     * Содержит значение если isComplete()==true или null если false или выставлена ошибка
     * Перед чтением необходимо проверять isPresent()
     * @return
     */
    public T getValue() {
        return value;
    }

    public synchronized void setValue(T value) {
        present = true;
        complete = true;
        this.value = value;
    }
}