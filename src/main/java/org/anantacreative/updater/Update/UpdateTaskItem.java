package org.anantacreative.updater.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class UpdateTaskItem {
    private List<UpdateActionFileItem> files = new ArrayList<>();
    private UpdateAction action;

    public UpdateTaskItem(List<UpdateActionFileItem> files, UpdateAction action) {
        this.files = files;
        this.action = action;
    }

    public UpdateTaskItem() {
    }

    public void setAction(UpdateAction action) {
        this.action = action;
    }

    public void addFileItem(UpdateActionFileItem item) {

        files.add(item);
    }

    public void execute() throws UpdateActionException {
        action.execute(files);
    }

    public List<UpdateActionFileItem> getAllFiles() {
        return files;
    }

    /**
     * Список файлов для загрузки. Те файлы, которые имеют атрибут url!=null
     *
     * @return
     */
    public List<UpdateActionFileItem> getDownloadingFiles() {
        return files.stream().filter(f -> f.getUrl() != null).collect(Collectors.toList());
    }
}
