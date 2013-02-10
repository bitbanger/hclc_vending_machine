import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;

public class ImageLoader
{
	private static final String IMAGE_ROOT = "/";
	private static final String PURCHASE_ITEM = "item_purchase.png";

	private ImageLoader() {}

	private static BufferedImage loadImageByName(String name)
	{
		try
		{
			return ImageIO.read(ImageLoader.class.getResource(IMAGE_ROOT + name));
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public static BufferedImage loadItemPurchaseImage()
	{
		return loadImageByName(PURCHASE_ITEM);
	}
}
