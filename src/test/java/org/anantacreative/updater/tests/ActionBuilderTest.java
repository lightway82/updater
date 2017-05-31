package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.ActionBuilder;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.Actions.*;
import org.anantacreative.updater.Update.UpdateAction;
import org.testng.annotations.Test;

/**
 *
 */
public class ActionBuilderTest {

    @Test
    public void buildCopyDir() {

        try {
            UpdateAction action = ActionBuilder.build(ActionType.COPY_DIR);
            if (!(action instanceof ActionCopyDir)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDir");

            action = ActionBuilder.build(ActionType.COPY_DIR.getTypeName());
            if (!(action instanceof ActionCopyDir)) org.testng.AssertJUnit.fail("Ожидается ActionCopyDir");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionCopyDir. Получено исключение ActionBuilder.UnknownActionError");
        }
    }



    @Test
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

    @Test
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

    @Test
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

    @Test
    public void buildDeleteDir() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.DELETE_DIR);
            if (!(action instanceof ActionDeleteDir)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDir");

            action = ActionBuilder.build(ActionType.DELETE_DIR.getTypeName());
            if (!(action instanceof ActionDeleteDir)) org.testng.AssertJUnit.fail("Ожидается ActionDeleteDir");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail(
                    "Ожидается ActionDeleteDir. Получено исключение ActionBuilder.UnknownActionError");
        }
    }

    @Test
    public void buildPackDir() {
        try {
            UpdateAction action = ActionBuilder.build(ActionType.PACK_DIR);
            if (!(action instanceof ActionPackDir)) org.testng.AssertJUnit.fail("Ожидается ActionPackDir");

            action = ActionBuilder.build(ActionType.PACK_DIR.getTypeName());
            if (!(action instanceof ActionPackDir)) org.testng.AssertJUnit.fail("Ожидается ActionPackDir");

        } catch (ActionBuilder.UnknownActionError e) {
            org.testng.AssertJUnit.fail("Ожидается ActionPackDir. Получено исключение ActionBuilder.UnknownActionError");
        }
    }

    @Test
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

    @Test
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

    @Test
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


    @Test
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

    @Test
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
