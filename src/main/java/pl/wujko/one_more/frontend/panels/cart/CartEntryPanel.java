package pl.wujko.one_more.frontend.panels.cart;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import pl.wujko.one_more.bean.BeanHelper;
import pl.wujko.one_more.code.constance.PizzaConstants;
import pl.wujko.one_more.code.item.Entry;
import pl.wujko.one_more.frontend.GUIConstants;
import pl.wujko.one_more.frontend.controller.CartListController;
import pl.wujko.one_more.frontend.datas.WorkshopData;
import pl.wujko.one_more.frontend.panels.entry.EntryPanel;
import pl.wujko.one_more.frontend.utils.FormLayoutUtils;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/**
 * Created by Agata on 2015-10-21.
 */

public class CartEntryPanel extends JPanel implements ActionListener
{
    private static final int STATIC_ELEMENTS_COUNT = 3;

    private WorkshopData workshopData;

    private JButton delete;

    private JButton edit;

    private JLabel priceLabel;

    private DefaultFormBuilder builder;

    private CellConstraints cc;

    private int currentRow = 1;

    private boolean discount = false;

    public CartEntryPanel(WorkshopData workshopData)
    {
        this.workshopData = workshopData;

        setBackground(GUIConstants.MAIN_PANEL_BACKGROUND);
        setLayout(new FormLayout("f:p:g", "f:m"));

        delete = new JButton("X");
        delete.addActionListener(this);
        delete.setFont(GUIConstants.DEFAULT_FONT);

        edit = new JButton(">");
        edit.setFont(GUIConstants.DEFAULT_FONT);
        edit.addActionListener(this);

        priceLabel = new JLabel("0.00", SwingConstants.CENTER);
        priceLabel.setFont(GUIConstants.DEFAULT_FONT);

        initPanel();
    }

    public int getPrice()
    {
        if (discount)
        {
            return workshopData.getPrice() - PizzaConstants.PIZZA_DISCOUNT;
        }
        return workshopData.getPrice();
    }

    public boolean isPizza()
    {
        return workshopData.isPizza();
    }

    public void setPizzaDiscount(boolean discount)
    {
        this.discount = discount;
        calculatePrice();
    }

    private void initPanel()
    {
        int maxSize = workshopData.size() + STATIC_ELEMENTS_COUNT;

        FormLayout layout = FormLayoutUtils.createDefaultEntryLayout(maxSize);
        builder = new DefaultFormBuilder(layout);
        cc = new CellConstraints();

        addToBuilder(workshopData.getAllEntries());

        calculatePrice();
        addToBuilder(priceLabel);
        addToBuilder(delete);
        addToBuilder(edit);

        add(builder.getPanel(), cc.xy(1, 1));
    }

    private void calculatePrice()
    {
        int integer = getPrice() / 100;
        int afterComa = getPrice() % 100;
        this.priceLabel.setText(integer + "." + afterComa + " zł");
    }

    private void addToBuilder(LinkedList<Entry> space)
    {
        for (Entry entry : space)
        {
            EntryPanel panel = new EntryPanel(entry);
            addToBuilder(panel);
        }
    }
    
    private void addToBuilder(Component panel)
    {
        builder.add(panel, cc.xy(currentRow, 1));
        currentRow += 2;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        //noinspection StatementWithEmptyBody
        if (source.equals(edit))
        {
            //todo aga
        }
        else if (source.equals(delete))
        {
            getCartListController().removeCartEntry(this);
        }
    }

    private CartListController getCartListController()
    {
        return (CartListController) BeanHelper.getBean("cartListController");
    }
}
