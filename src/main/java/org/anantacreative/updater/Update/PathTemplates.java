package org.anantacreative.updater.Update;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.anantacreative.updater.Utilites.FilesUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Определяет шаблоны для переменных, в путях UpdateActionFileItem, например, переменная пути к директории загрузок.
 * При создании своей стратегии обновления можно добавлять свои значения через setVar(String var, String val)
 */
public class PathTemplates {

    private PathTemplates() {
    }

    private static String headDelimiter = "${";
    private static String tailDelimiter = "}";

    private static Map<String, Variable> varsMap = new HashMap<>();
    public static final String DOWNLOAD_DIR = "DOWNLOAD_DIR";

    static {
        varsMap.put(DOWNLOAD_DIR, new Variable(DOWNLOAD_DIR,""));
    }

    /**
     * Устанавливает значение шаблонной переменной. Если переменной еще нет, то она будет создана.
     * @param var имя переменной
     * @param val присваемое значение
     */
    public static void setVar(String var, String val) {
        if(varsMap.containsKey(var))varsMap.get(var).setVal( FilesUtil.replaceAllBackSlashes(val).trim());
        else varsMap.put(var, new Variable(var,FilesUtil.replaceAllBackSlashes(val).trim()));
    }

    public static Collection<Variable> getAllVars(){
        return varsMap.values();
    }


    /**
     * Заменяет все шаблонные переменные на значения в строке.
     * Считается что все пути в качестве разделителей содержат только слэши(/),
     * тк фреймворк автоматически их подменяет при создании UpdateActionFileItem
     * Корректно расставит / в путях
     * переменные просто заменяются. Если в переменной есть начальный или(и) оконечный слэш, то будет проверка окружающей строки на их наличие.
     * @param src строка содержащая шаблонные переменные
     * @return строка с замененными значениями шаблонных переменных
     */
    public static String replaceVarsInPath(String src) {
        String result = FilesUtil.replaceAllSpaces(src);
        for (Map.Entry<String, Variable> entry : varsMap.entrySet()) {
            result = replaceVarInPath( entry.getKey(), entry.getValue().getVal(), result);
        }

        return result;
    }

    private static String replaceVarInPath(String var, String val, String src){
        String template = headDelimiter+var+tailDelimiter;
        int startIndex = src.indexOf(template);
        if(startIndex==-1) return src;
        return FilesUtil.replaceDuplicatedSlashes(src.replace(template,val));
    }

    @Data
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public static final  class Variable{
        private String name;
        private String val;

    }

}
