package org.anantacreative.updater.Update;

import org.anantacreative.updater.Utilites.CommonUtils;

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
     * @param listener слушатель событий процесса обновления
     * @return число элементов UpdateAction в обновлении
     */
    public int update(UpdateListener listener) {
        if(listener == null) throw new RuntimeException("UpdateListener is NULL");
        final int totalActions = this.updateItems.size();
        Thread thread = new Thread(() -> {
            try {
                for (int i=0; i < updateItems.size(); i++) {
                    updateItems.get(i).execute();
                    listener.progress(CommonUtils.calculatePersent(i+1, totalActions));
                }
                listener.completed();
            } catch (UpdateActionException e) {
                listener.error(new UpdateException(e));
            }catch (Exception e){
                listener.error(new UpdateException(e));
            }
        });
        thread.start();
        return totalActions;
    }




    /**
     * Список файлов для загрузки
     *
     * @return
     */
    public List<UpdateActionFileItem> getDownloadingFilesItem() {
        return updateItems.stream().flatMap(f -> f.getDownloadingFiles().stream()).filter(f->f.getUrl()!=null).collect(Collectors.toList());
    }

    public interface UpdateListener{
        void progress(int persent);
        void completed();
        void error(UpdateException e);
    }

}
