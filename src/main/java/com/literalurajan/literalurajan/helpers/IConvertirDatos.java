package com.literalurajan.literalurajan.helpers;

public interface IConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}