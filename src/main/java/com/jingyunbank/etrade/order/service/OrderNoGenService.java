package com.jingyunbank.etrade.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jingyunbank.etrade.api.order.service.IOrderNoGenService;

@Service("orderNoGenService")
public class OrderNoGenService implements IOrderNoGenService {

	private String MID;
	private String UID;
	
	@Override
	public IOrderNoGenService setMID(String mid) {
		this.MID = mid;
		return this;
	}

	@Override
	public IOrderNoGenService setUID(String uid) {
		this.UID = uid;
		return this;
	}

	@Override
	public long next() {
		long result = gen();
		//ensure the sequence generated is bigger than 10 length
		while(result < 9999999999L){
			result = gen();
		}
		return result;
	}

	private long gen (){
		UUID uid = UUID.randomUUID();
		long f = uid.getLeastSignificantBits();
    	long l = uid.getMostSignificantBits();
    	long sd = f + l;
		long midhash = this.MID.hashCode();
		long uidhash = this.UID.hashCode();
		long re = (sd>0?sd:-sd)+(midhash<0?-midhash:midhash)+(uidhash<0?-uidhash:uidhash);
		return re;
	}
	
	@Override
	public String nexts() {
		return String.valueOf(next());
	}

}
