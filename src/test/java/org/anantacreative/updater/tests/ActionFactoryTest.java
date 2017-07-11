package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.Actions.ActionFactory;
import org.anantacreative.updater.Update.Actions.ActionType;
import org.anantacreative.updater.Update.Actions.*;
import org.anantacreative.updater.Update.UpdateAction;
import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = {"common"})
public class ActionFactoryTest {


    public void buildCopyDir() {

        try {
            UpdateAction action = ActionFactory.build(ActionType.COPY_DIRS);
            if (!(action instanceof ActionCopyDirs)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs");

            action = ActionFactory.build(ActionType.COPY_DIRS.getTypeName());
            if (!(action instanceof ActionCopyDirs)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs. Получено исключение ActionFactory.UnknownActionError");
        }
    }

    public void download() {

        try {
            UpdateAction action = ActionFactory.build(ActionType.DOWNLOAD);
            if (!(action instanceof ActionDownload)) org.testng.AssertJUnit.fail("Ожидается ActionDownload");

            action = ActionFactory.build(ActionType.DOWNLOAD.getTypeName());
            if (!(action instanceof ActionDownload)) org.testng.AssertJUnit.fail("Ожидается ActionDownload");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionDownload. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildCopyFiles() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.COPY_FILES);
            if (!(action instanceof ActionCopyFiles)) org.testng.AssertJUnit.fail("Ожидается ActionCopyFiles");

            action = ActionFactory.build(ActionType.COPY_FILES.getTypeName());
            if (!(action instanceof ActionCopyFiles)) org.testng.AssertJUnit.fail("Ожидается ActionCopyFiles");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionCopyFiles. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildMove() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.MOVE);
            if (!(action instanceof ActionMove)) org.testng.AssertJUnit.fail("Ожидается ActionMove");

            action = ActionFactory.build(ActionType.MOVE.getTypeName());
            if (!(action instanceof ActionMove)) org.testng.AssertJUnit.fail("Ожидается ActionMove");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionMove. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildDeleteFiles() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.DELETE_FILES);
            if (!(action instanceof ActionDeleteFiles)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteFiles");

            action = ActionFactory.build(ActionType.DELETE_FILES.getTypeName());
            if (!(action instanceof ActionDeleteFiles)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteFiles");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionDeleteFiles. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildDeleteDir() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.DELETE_DIRS);
            if (!(action instanceof ActionDeleteDirs)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDirs");

            action = ActionFactory.build(ActionType.DELETE_DIRS.getTypeName());
            if (!(action instanceof ActionDeleteDirs)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDirs");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionDeleteDirs. Получено исключение ActionFactory.UnknownActionError");
        }
    }




    public void buildPackFiles() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.PACK_FILES);
            if (!(action instanceof ActionPackFiles)) org.testng.AssertJUnit.fail("Ожидается ActionPackFiles");

            action = ActionFactory.build(ActionType.PACK_FILES.getTypeName());
            if (!(action instanceof ActionPackFiles)) org.testng.AssertJUnit.fail("Ожидается ActionPackFiles");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionPackFiles. Получено исключение ActionFactory.UnknownActionError");
        }
    }

    public void buildPackDirs() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.PACK_DIRS);
            if (!(action instanceof ActionPackDirs)) org.testng.AssertJUnit.fail("Ожидается ActionPackDirs");

            action = ActionFactory.build(ActionType.PACK_DIRS.getTypeName());
            if (!(action instanceof ActionPackDirs)) org.testng.AssertJUnit.fail("Ожидается ActionPackDirs");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionPackDirs. Получено исключение ActionFactory.UnknownActionError");
        }
    }

    public void buildRename() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.RENAME);
            if (!(action instanceof ActionRename)) org.testng.AssertJUnit.fail("Ожидается ActionRename");

            action = ActionFactory.build(ActionType.RENAME.getTypeName());
            if (!(action instanceof ActionRename)) org.testng.AssertJUnit.fail("Ожидается ActionRename");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionRename. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildRun() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.RUN);
            if (!(action instanceof ActionRun)) org.testng.AssertJUnit.fail("Ожидается ActionRun");

            action = ActionFactory.build(ActionType.RUN.getTypeName());
            if (!(action instanceof ActionRun)) org.testng.AssertJUnit.fail("Ожидается ActionRun");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionRun. Получено исключение ActionFactory.UnknownActionError");
        }
    }



    public void buildUnpack() {
        try {
            UpdateAction action = ActionFactory.build(ActionType.UNPACK);
            if (!(action instanceof ActionUnpack)) org.testng.AssertJUnit.fail("Ожидается ActionUnpack");

            action = ActionFactory.build(ActionType.UNPACK.getTypeName());
            if (!(action instanceof ActionUnpack)) org.testng.AssertJUnit.fail("Ожидается ActionUnpack");

        } catch (ActionFactory.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionUnpack. Получено исключение ActionFactory.UnknownActionError");
        }
    }


    public void buildUnknown() {

        try {
            ActionFactory.build("qwqdas");
            org.testng.AssertJUnit.fail("Должно быть исключение ActionFactory.UnknownActionError");
        } catch (ActionFactory.UnknownActionError e) {

        }

        try {
            ActionFactory.build(ActionType.UNKNOWN);
            org.testng.AssertJUnit.fail("Должно быть исключение ActionFactory.UnknownActionError");
        } catch (ActionFactory.UnknownActionError e) {

        }

    }
}
