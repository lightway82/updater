package org.anantacreative.updater.Update;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Представляет собой сформированное задание на обновление, запуск которого произведет все процедуры по обновлению
 */
public class UpdateTask {
    private List<UpdateTaskItem> updateItems = new ArrayList<>();
    private Date releaseDate;

    private UpdateTask() {
    }

    public UpdateTask(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public UpdateTask(List<UpdateTaskItem> updateItems, Date releaseDate) {
        this.updateItems = updateItems;
        this.releaseDate = releaseDate;
    }

    /**
     * Добавление нового Action
     *
     * @param item
     */
    public void addItem(UpdateTaskItem item) {
        updateItems.add(item);
    }

    /**
     * Запускает процесс выполение списка Action  для обновления
     */
    public void update() {
        for (UpdateTaskItem item : updateItems) item.execute();

    }

    /**
     * Список файлов для загрузки
     *
     * @return
     */
    public List<UpdateActionFileItem> getDownloadingFiles() {
        return updateItems.stream().flatMap(f -> f.getDownloadingFiles().stream()).collect(Collectors.toList());
    }
}
