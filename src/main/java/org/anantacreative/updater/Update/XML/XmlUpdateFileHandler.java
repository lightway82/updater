package org.anantacreative.updater.Update.XML;

import org.anantacreative.updater.Update.Actions.ActionBuilder;
import org.anantacreative.updater.Update.UpdateActionFileItem;
import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.Update.UpdateTaskItem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * handler для {@link XmlUpdateFileParser}
 */
public class XmlUpdateFileHandler extends DefaultHandler {


    private UpdateTask task;
    private UpdateTaskItem currentUpdateTaskItem;
    private boolean isUpdateFile = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.YYYY");
    private File rootDirApp;


    public XmlUpdateFileHandler(File rootDirApp) {
        super();
        this.rootDirApp = rootDirApp;
    }

    @Override
    public void startElement(String uri, String name, String qName, Attributes attrs) throws SAXException {
        if(qName.equals("Update")) {
            parseUpdateTag(attrs);
            return;

        } else if(qName.equals("Action")) {
            parseActionTag(attrs);

        } else if(qName.equals("File")) {
            parseFileTag(attrs);
        }


    }

    private void parseFileTag(Attributes attrs) throws SAXException {
        if (!isUpdateFile) throw new SAXException("File has not correct format.");
        if (currentUpdateTaskItem == null) throw new SAXException("File has not correct format.");

        String url = attrs.getValue("url");
        String src = attrs.getValue("src");
        String dst = attrs.getValue("dst");

        UpdateActionFileItem.Builder builder = UpdateActionFileItem.create();
        if (url != null) try {
            if (!url.isEmpty()) builder.setURL(new URL(url));
        } catch (MalformedURLException e) {
            throw new SAXException("URL is not allowed.", e);
        }

        if (src != null) {
            if (!src.isEmpty()) builder.setSrcPath(src,rootDirApp);
        }
        if (dst != null) {
            if (!dst.isEmpty()) builder.setDstPath( dst,rootDirApp);
        }
        currentUpdateTaskItem.addFileItem(builder.build());
    }

    private void parseActionTag(Attributes attrs) throws SAXException {
        if (!isUpdateFile) throw new SAXException("File has not correct format.");
        String type = attrs.getValue("type");
        if (type == null) throw new SAXException("File has not correct format. Action tag has not type attribute");


        currentUpdateTaskItem = new UpdateTaskItem();
        try {
            currentUpdateTaskItem.setAction(ActionBuilder.build(type));
        } catch (ActionBuilder.UnknownActionError e) {
            throw new SAXException("Action type='"+type+"'",e);
        }
    }

    private void parseUpdateTag(Attributes attrs) throws SAXException {
        String date = attrs.getValue("date");
        if (date == null) throw new SAXException("File has not correct format. Update date absent.");
        if (date.isEmpty()) throw new SAXException("File has not correct format. Update date do not be empty");

        try {
            task = new UpdateTask(sdf.parse(date));
            isUpdateFile = true;
        } catch (ParseException e) {
            throw new SAXException("File has not correct format. Update date must be dd.MM.YYYY format.");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        if (qName.equals("Action")) {
            task.addItem(currentUpdateTaskItem);
            currentUpdateTaskItem = null;
        }

    }

    public UpdateTask getTask() {
        return task;
    }
}

