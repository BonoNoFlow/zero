package com.bono.soundcloud;

import com.bono.api.SoundcloudSearch;
import com.bono.command.DBExecutor;
import com.bono.command.MPDCommand;
import com.bono.Utils;
import com.bono.view.SoundcloudView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 19/02/16.
 */
public class SoundcloudController extends MouseAdapter implements ActionListener {

    private String clientId = "93624d1dac08057730320d42ba5a0bdc";

    private SoundcloudView soundcloudView;
    private SoundcloudSearch soundcloudSearch;
    private DefaultListModel<Result> listModel;

    private DBExecutor dbExecutor;

    @Deprecated
    public SoundcloudController() {
        init();
    }

    public SoundcloudController(DBExecutor dbExecutor, SoundcloudView soundcloudView) {
        this.dbExecutor = dbExecutor;
        this.soundcloudView = soundcloudView;
        init();
    }

    private void init() {
        listModel = new DefaultListModel<>();

        soundcloudView.addSearchListener(this);
        soundcloudView.addMouseListener(this);
    }

    public SoundcloudSearch getSoundcloudSearch() {
        return soundcloudSearch;
    }

    public void setSoundcloudView(SoundcloudView soundcloudView) {
        this.soundcloudView = soundcloudView;
        soundcloudView.getSearchField().addActionListener(this);
    }

    public DefaultListModel<Result> getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel<Result> listModel) {
        this.listModel = listModel;
    }

    public void setSoundcloudSearch(SoundcloudSearch soundcloudSearch) {
        this.soundcloudSearch = soundcloudSearch;
    }

    public SoundcloudView getSoundcloudView() {
        return soundcloudView;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        /*
        Het laden van de artwork moet een class worden.
         */
        listModel = new DefaultListModel<>();
        soundcloudSearch = new SoundcloudSearch(clientId);
        soundcloudView.clearSearchField();
        JSONArray response = soundcloudSearch.searchTracks(e.getActionCommand());

        Iterator iterator = response.iterator();
        while (iterator.hasNext()) {
            JSONObject object = (JSONObject) iterator.next();
            int seconds = (Integer) object.get("duration");
            Duration duration = Duration.ofMillis(seconds);

            String time = Utils.time(duration);

            Result result = new Result(object.getString("permalink_url"), object.getString("title"), time);

            String urlString = "";
            if (!object.get("artwork_url").equals(null)) {
                urlString = String.valueOf(object.get("artwork_url"));

                urlString = urlString.replaceAll("large", "tiny");

                Image image = null;
                try {
                    URL url = new URL(urlString);
                    image = ImageIO.read(url);
                    /*
                    Handel unsupported image exception af!!!!
                     */
                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage() + " " + urlString);
                    ioe.printStackTrace();
                }
                if (image != null) {
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);
                } else {
                    System.out.println("image null! ");

                    //Image image1 = null;
                    try {
                        URL url = this.getClass().getResource("/default-icon.png");
                        image = ImageIO.read(url);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    result.setImage(image);
                }
            } else {
                System.out.println("artwork url null! ");

                Image image = null;
                try {
                    URL url = this.getClass().getResource("/default-icon.png");
                    image = ImageIO.read(url);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                image = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                result.setImage(image);
            }

            listModel.addElement(result);
        }
        soundcloudView.getResultList().setModel(listModel);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        //System.out.println("clicked" + e.getButton() + MouseEvent.BUTTON3);
        if (e.getButton() == MouseEvent.BUTTON3) {
            //System.out.println("clicked!!!!!");
            ListSelectionModel model = ((JList) e.getSource()).getSelectionModel();

            if (!model.isSelectionEmpty()) {
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem addItem = new JMenuItem("add");
                addItem.addActionListener(event -> {

                    int track = model.getAnchorSelectionIndex();

                    String reply = "";
                    try {
                        reply = dbExecutor.execute(new MPDCommand("load", Utils.loadUrl(listModel.get(track).getUrl())));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
                popupMenu.add(addItem);
                popupMenu.show(soundcloudView.getResultList(), e.getX(), e.getY());
            }
        }
    }
}
