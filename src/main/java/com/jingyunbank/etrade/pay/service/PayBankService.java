package com.jingyunbank.etrade.pay.service;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.jingyunbank.etrade.api.pay.bo.PayBank;
import com.jingyunbank.etrade.api.pay.service.IPayBankService;
import com.jingyunbank.etrade.pay.dao.PayBankDao;

@Service("payBankService")
public class PayBankService implements IPayBankService {

	@Autowired
	private PayBankDao payBankDao;
	
	@Override
	public List<PayBank> listFastDebitWayBanks() {
		return payBankDao.select(false, false, false, true)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listFastDebitWayBanks(long from, long to) {
		return payBankDao.selectRange(false, false, false, true, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listFastCreditWayBanks() {
		return payBankDao.select(false, false, true, false)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listFastCreditWayBanks(long from, long to) {
		return payBankDao.selectRange(false, false, true, false, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateCreditWayBanks() {
		return payBankDao.select(true, false, false, false)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateCreditWayBanks(long from, long to) {
		return payBankDao.selectRange(true, false, false, false, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateDebitWayBanks() {
		return payBankDao.select(false, true, false, false)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateDebitWayBanks(long from, long to) {
		return payBankDao.selectRange(false, true, false, false, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listFastWayBanks() {
		return payBankDao.select(false, false, true, true)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listFastWayBanks(long from, long to) {
		return payBankDao.selectRange(false, false, true, true, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateWayBanks() {
		return payBankDao.select(true, true, false, false)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

	@Override
	public List<PayBank> listGateWayBanks(long from, long to) {
		return payBankDao.selectRange(true, true, false, false, from, to-from)
				.stream().map(entity->{
					PayBank bo = new PayBank();
					BeanUtils.copyProperties(entity, bo);
					return bo;
				}).collect(Collectors.toList());
	}

}
