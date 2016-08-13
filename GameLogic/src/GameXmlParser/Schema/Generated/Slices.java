
package GameXmlParser.Schema.Generated;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}Slice" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "slice"
})
@XmlRootElement(name = "Slices")
public class Slices {

    @XmlElement(name = "Slice", required = true)
    protected List<Slice> slice;

    /**
     * Gets the value of the slice property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slice property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlice().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Slice }
     */
    public List<Slice> getSlice() {
        if (slice == null) {
            slice = new ArrayList<Slice>();
        }
        return this.slice;
    }

}
