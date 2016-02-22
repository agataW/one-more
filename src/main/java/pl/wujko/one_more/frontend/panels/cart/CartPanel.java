package pl.wujko.one_more.frontend.panels.cart;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.apache.commons.collections.CollectionUtils;
import pl.wujko.one_more.code.constance.PanSize;
import pl.wujko.one_more.code.constance.PanType;
import pl.wujko.one_more.code.constance.PizzaConstants;
import pl.wujko.one_more.code.item.Entry;
import pl.wujko.one_more.code.item.entries.Addition;
import pl.wujko.one_more.frontend.GUIConstants;
import pl.wujko.one_more.frontend.datas.WorkshopData;
import pl.wujko.one_more.frontend.utils.FormLayoutUtils;

import javax.swing.JPanel;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Agata on 2015-10-21.
 */
public class CartPanel extends JPanel
{
    private DefaultFormBuilder builder;

    private CellConstraints cc;

    private CartHeaderPanel headerPanel;

    private List<CartEntryPanel> cartEntryPanelList;

    private AdditionCartEntry lastAdditionCartEntry;

    private int currentRow = 1;

    private boolean selected;

    public CartPanel()
    {
        headerPanel = new CartHeaderPanel(this);
        cartEntryPanelList = new LinkedList<CartEntryPanel>();

        setBackground(GUIConstants.CART_HEADER_PANEL_BACKGROUND);
        setLayout(new FormLayout("f:p:g", "f:m"));

        initPanel();
    }

    public void createCartEntry(WorkshopData workshop)
    {
        CartEntryPanel cartEntryPanel = new CartEntryPanel(workshop);
        cartEntryPanelList.add(cartEntryPanel);
        initPanel();
        calculatePrice();
    }

    public void removeEntry(CartEntryPanel cartEntryPanel)
    {
        cartEntryPanelList.remove(cartEntryPanel);
        initPanel();
        calculatePrice();
        revalidate();
    }

    public boolean contains(CartEntryPanel cartEntryPanel)
    {
        return cartEntryPanelList.contains(cartEntryPanel);
    }

    public void addAddition(Addition addition)
    {
        int index = cartEntryPanelList.size();
        if (lastAdditionCartEntry == null || lastAdditionCartEntry.full() || cartEntryPanelList
            .indexOf(lastAdditionCartEntry.getCartEntryPanel()) == -1)
        {
            lastAdditionCartEntry = new AdditionCartEntry();
        }
        else
        {
            index = cartEntryPanelList.indexOf(lastAdditionCartEntry.getCartEntryPanel());
            removeEntry(lastAdditionCartEntry.getCartEntryPanel());
        }
        lastAdditionCartEntry.add(addition);

        CartEntryPanel cartEntryPanel = new CartEntryPanel(lastAdditionCartEntry.createWorkshopData());
        cartEntryPanel.disableEditButton();
        cartEntryPanelList.add(index, cartEntryPanel);
        calculatePrice();
        initPanel();

        lastAdditionCartEntry.setCartEntryPanel(cartEntryPanel);
    }

    private int pizzaCount()
    {
        int pizzaCount = 0;
        for (CartEntryPanel cartEntryPanel : cartEntryPanelList)
        {
            if (cartEntryPanel.isPizza())
            {
                ++pizzaCount;
            }
        }
        return pizzaCount;
    }

    private void calculatePrice()
    {
        int price = 0;
        for (CartEntryPanel cartEntryPanel : cartEntryPanelList)
        {
            cartEntryPanel.setPizzaDiscount(pizzaCount() >= PizzaConstants.COUNT_OF_PIZZA_TO_DISCOUNT);
        }

        for (CartEntryPanel cartEntryPanel : cartEntryPanelList)
        {
            price += cartEntryPanel.getPrice();
        }
        headerPanel.setPrice(price);
    }

    private void initPanel()
    {
        cc = new CellConstraints();
        initBuilder();
        addToBuilder(headerPanel);
        if (selected)
        {
            addToBuilder(cartEntryPanelList);
        }
    }

    private void addToBuilder(List<CartEntryPanel> cartEntryPanelList)
    {
        if (CollectionUtils.isNotEmpty(cartEntryPanelList))
        {
            for (CartEntryPanel cartEntryPanel : cartEntryPanelList)
            {
                addToBuilder(cartEntryPanel);
            }
        }
    }

    private void addToBuilder(Component component)
    {
        builder.add(component, cc.xy(1, currentRow));
        currentRow += 2;
        add(builder.getPanel(), cc.xy(1, 1));
    }

    private void initBuilder()
    {
        currentRow = 1;
        int size = 0;
        if (selected)
        {
            size = cartEntryPanelList.size();
        }
        FormLayout layout = FormLayoutUtils.createCartListLayout(size);
        builder = new DefaultFormBuilder(layout);
        removeAll();
    }

    public void setSelected(boolean selected)
    {
        if (this.selected != selected)
        {
            this.selected = selected;
            initPanel();
        }
    }

    private class AdditionCartEntry
    {
        private CartEntryPanel cartEntryPanel;

        private LinkedList<Entry> additionList;

        public void add(Addition addition)
        {
            if (additionList == null)
            {
                additionList = new LinkedList<Entry>();
            }
            additionList.add(addition);
        }

        public void setCartEntryPanel(CartEntryPanel cartEntryPanel)
        {
            this.cartEntryPanel = cartEntryPanel;
        }

        public CartEntryPanel getCartEntryPanel()
        {
            return cartEntryPanel;
        }

        public boolean full()
        {
            return additionList.size() == 5;
        }

        public WorkshopData createWorkshopData()
        {
            WorkshopData workshopData = new WorkshopData();
            workshopData.setPanType(PanType.NO_PANE);
            workshopData.setPanSize(PanSize.NO_PAN);
            workshopData.setWholeSpace(additionList);
            return workshopData;
        }
    }
}
