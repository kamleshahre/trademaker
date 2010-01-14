package org.lifeform.pricer;

import java.util.Calendar;
import java.util.Currency;

import org.lifeform.market.Curve;
import org.lifeform.market.Market;
import org.lifeform.market.Quote;
import org.lifeform.product.Bond;
import org.lifeform.product.Product;
import org.lifeform.time.Period;
import org.lifeform.time.Term;

public class BondTreasury extends Pricer {
	@Override
	public PricingResult price(final Product product, final Calendar valuationDate,
			final Market market) {
		PricingResult result = new PricingResult(product, valuationDate);
		Bond bond = (Bond) product;
		// Currency cur = product.getCurrency();
		// Curve zc = market.getDiscountCurve(cur);
		double time = bond.time();
		double price = bond.getPrice().getValue();
		result.put(PricingResult.Type.Yield, (bond.getFaceValue() / price - 1)
				/ time);
		return result;
	}

	public static void main(final String[] args) {

		Calendar valuationDate = Calendar.getInstance();

		Bond bond = new Bond(-1);
		bond.setCurrency(Currency.getInstance("USD"));
		bond.setFaceValue(100);
		bond.setAccrual(new Period(Calendar.getInstance(), new Term(3,
				Term.Type.M)));
		bond.setPrice(new Quote(Quote.Type.Price, 97.5));

		Market market = new Market("Simple", Calendar.getInstance());
		Curve c = new Curve("DISC", Currency.getInstance("USD"), Calendar
				.getInstance());
		market.put("Discount.USD", c);

		Pricer pricer = new BondTreasury();
		pricer.price(bond, valuationDate, market);
		PricingResult result = pricer.price(bond, valuationDate, market);

		result.dump(System.out);
	}

}
