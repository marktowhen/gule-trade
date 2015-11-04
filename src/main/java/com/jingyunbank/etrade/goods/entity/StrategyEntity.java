package com.jingyunbank.etrade.goods.entity;

import java.math.BigDecimal;
/**
 * 策略表信息
 * @author liug
 *
 */
public class StrategyEntity {
	/**主键 */
	private String ID;
	/**策略名称 */
	private String strategyName;
	/**参数1 */
	private BigDecimal parameter1;
	/**参数2 */
	private BigDecimal parameter2;
	/**参数3*/
	private BigDecimal parameter3;
	/**参数4 */
	private BigDecimal parameter4;
	/**策略描述 */
	private String description;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public BigDecimal getParameter1() {
		return parameter1;
	}
	public void setParameter1(BigDecimal parameter1) {
		this.parameter1 = parameter1;
	}
	public BigDecimal getParameter2() {
		return parameter2;
	}
	public void setParameter2(BigDecimal parameter2) {
		this.parameter2 = parameter2;
	}
	public BigDecimal getParameter3() {
		return parameter3;
	}
	public void setParameter3(BigDecimal parameter3) {
		this.parameter3 = parameter3;
	}
	public BigDecimal getParameter4() {
		return parameter4;
	}
	public void setParameter4(BigDecimal parameter4) {
		this.parameter4 = parameter4;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
