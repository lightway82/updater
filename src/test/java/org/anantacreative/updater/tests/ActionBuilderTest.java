package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.Actions.ActionBuilder;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.Actions.*;
import org.anantacreative.updater.Update.UpdateAction;
import org.testng.annotations.Test;

/**
 *
 */
@Test(groups = {"common"})
public class ActionBuilderTest {


    public void buildCopyDir() {

        try {
            UpdateAction action = ActionBuilder.build(ActionType.COPY_DIRS);
            if (!(action instanceof ActionCopyDirs)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs");

            action = ActionBuilder.build(ActionType.COPY_DIRS.getTypeName());
            if (!(action instanceof ActionCopyDirs)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionCopyDirs. Получено исключение ActionBuilder.UnknownActionError");
        }
    }

    public void download() {

        try {
            UpdateAction action = ActionBuilder.build(ActionType.DOWNLOAD);
            if (!(action instanceof ActionDownload)) org.testng.AssertJUnit.fail("Ожидается ActionDownload");

            action = ActionBuilder.build(ActionType.DOWNLOAD.getTypeName());
            if (!(action instanceof ActionDownload)) org.testng.AssertJUnit.fail("Ожидается ActionDownload");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionDownload. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildCopyFiles() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.COPY_FILES);
            if (!(action instanceof ActionCopyFiles)) org.testng.AssertJUnit.fail("Ожидается ActionCopyFiles");

            action = ActionBuilder.build(ActionType.COPY_FILES.getTypeName());
            if (!(action instanceof ActionCopyFiles)) org.testng.AssertJUnit.fail("Ожидается ActionCopyFiles");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionCopyFiles. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildMove() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.MOVE);
            if (!(action instanceof ActionMove)) org.testng.AssertJUnit.fail("Ожидается ActionMove");

            action = ActionBuilder.build(ActionType.MOVE.getTypeName());
            if (!(action instanceof ActionMove)) org.testng.AssertJUnit.fail("Ожидается ActionMove");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionMove. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildDeleteFiles() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.DELETE_FILES);
            if (!(action instanceof ActionDeleteFiles)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteFiles");

            action = ActionBuilder.build(ActionType.DELETE_FILES.getTypeName());
            if (!(action instanceof ActionDeleteFiles)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteFiles");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionDeleteFiles. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildDeleteDir() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.DELETE_DIRS);
            if (!(action instanceof ActionDeleteDirs)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDirs");

            action = ActionBuilder.build(ActionType.DELETE_DIRS.getTypeName());
            if (!(action instanceof ActionDeleteDirs)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDirs");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionDeleteDirs. Получено исключение ActionBuilder.UnknownActionError");
        }
    }




    public void buildPackFiles() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.PACK_FILES);
            if (!(action instanceof ActionPackFiles)) org.testng.AssertJUnit.fail("Ожидается ActionPackFiles");

            action = ActionBuilder.build(ActionType.PACK_FILES.getTypeName());
            if (!(action instanceof ActionPackFiles)) org.testng.AssertJUnit.fail("Ожидается ActionPackFiles");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionPackFiles. Получено исключение ActionBuilder.UnknownActionError");
        }
    }

    public void buildPackDirs() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.PACK_DIRS);
            if (!(action instanceof ActionPackDirs)) org.testng.AssertJUnit.fail("Ожидается ActionPackDirs");

            action = ActionBuilder.build(ActionType.PACK_DIRS.getTypeName());
            if (!(action instanceof ActionPackDirs)) org.testng.AssertJUnit.fail("Ожидается ActionPackDirs");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionPackDirs. Получено исключение ActionBuilder.UnknownActionError");
        }
    }

    public void buildRename() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.RENAME);
            if (!(action instanceof ActionRename)) org.testng.AssertJUnit.fail("Ожидается ActionRename");

            action = ActionBuilder.build(ActionType.RENAME.getTypeName());
            if (!(action instanceof ActionRename)) org.testng.AssertJUnit.fail("Ожидается ActionRename");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionRename. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildRun() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.RUN);
            if (!(action instanceof ActionRun)) org.testng.AssertJUnit.fail("Ожидается ActionRun");

            action = ActionBuilder.build(ActionType.RUN.getTypeName());
            if (!(action instanceof ActionRun)) org.testng.AssertJUnit.fail("Ожидается ActionRun");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionRun. Получено исключение ActionBuilder.UnknownActionError");
        }
    }



    public void buildUnpack() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.UNPACK);
            if (!(action instanceof ActionUnpack)) org.testng.AssertJUnit.fail("Ожидается ActionUnpack");

            action = ActionBuilder.build(ActionType.UNPACK.getTypeName());
            if (!(action instanceof ActionUnpack)) org.testng.AssertJUnit.fail("Ожидается ActionUnpack");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionUnpack. Получено исключение ActionBuilder.UnknownActionError");
        }
    }


    public void buildUnknown() {

        try {
            ActionBuilder.build("qwqdas");
            org.testng.AssertJUnit.fail("Должно быть исключение ActionBuilder.UnknownActionError");
        } catch (ActionBuilder.UnknownActionError e) {

        }

        try {
            ActionBuilder.build(ActionType.UNKNOWN);
            org.testng.AssertJUnit.fail("Должно быть исключение ActionBuilder.UnknownActionError");
        } catch (ActionBuilder.UnknownActionError e) {

        }

    }
}
