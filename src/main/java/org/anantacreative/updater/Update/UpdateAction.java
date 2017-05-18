package org.anantacreative.updater.Update;

import java.util.List;

public interface UpdateAction {
    void execute(List<UpdateActionFileItem> files)throws UpdateActionException;


}
