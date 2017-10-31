package com.yq.controller.address;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.change.controller.base.BaseController;
import org.change.util.PageData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yq.service.address.AddressManager;
import com.yq.util.DatetimeUtil;
import com.yq.util.StringUtil;

/** 
 * 说明：收货地址
 * 创建人：摩里科技 qq
 * 创建时间：2017-02-21
 */
@Controller
public class AddressController extends BaseController {
	
	String menuUrl = "address/list.do"; //菜单地址(权限用)
	@Resource(name="addressService")
	private AddressManager addressService;
	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	
	@RequestMapping(value="/center/address/tolist")
	public ModelAndView tolist() throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("address/list");
		return mv;
	}
	
	@RequestMapping(value="/order/address/tolist")
	public ModelAndView ordertolist() throws Exception {
		PageData pd = new PageData();
		pd = this.getPageData();
		ModelAndView mv = new ModelAndView();
		mv.setViewName("order/address-list");
		mv.addObject("pd", pd);
		return mv;
	}
	
	@RequestMapping(value="/center/address/info")
	public ModelAndView info() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String msg = pd.getString("msg");
		if(msg.equals("edit")){
			pd = addressService.findById(pd);
		}
		mv.setViewName("address/info");
		mv.addObject("pd", pd);
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value="/order/address/info")
	public ModelAndView orderinfo() throws Exception {
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		String msg = pd.getString("msg");
		if(msg.equals("edit")){
			pd = addressService.findById(pd);
		}
		mv.setViewName("order/address-info");
		mv.addObject("pd", pd);
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value="/setaddress")
	public ModelAndView setaddress() throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.addObject("msg", "save");
		mv.setViewName("address/content");
		return mv;
	}
	@RequestMapping(value="/setalipay")
	public ModelAndView setalipay() throws Exception{
		ModelAndView mv = new ModelAndView();
		mv.setViewName("address/alipay");
		return mv;
	}

	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/address/save", produces = "application/json;charset=UTF-8")
	public String save() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		PageData pd = new PageData();
		pd = this.getPageData();
		String is_default = pd.getString("is_default");
		
		pd.put("addtime", DatetimeUtil.getDatetime());
		pd.put("user_id",shopUser.get("user_id"));
		pd.put("address_id", this.get32UUID());	//主键
		if(StringUtils.isNotEmpty(is_default)){//如果新增的地址设置为默认地址，更改用户其他地址为非默认
			addressService.updatedefault(pd);
		}
		addressService.save(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map);  
	}
	
	@ResponseBody
	@RequestMapping(value="/address/delete", produces = "application/json;charset=UTF-8")
	public String delete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		addressService.delete(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map); 
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/address/edit", produces = "application/json;charset=UTF-8")
	public String edit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime = sf.format(new Date());
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		pd.put("addtime", addtime);
		pd.put("user_id",shopUser.get("user_id"));
		String is_default = pd.getString("is_default");
		if(StringUtils.isNotEmpty(is_default)){//如果地址设置为默认地址，更改用户其他地址为非默认
			addressService.updatedefault(pd);
		}
		addressService.edit(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map);  
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/addresslist")
	public ModelAndView list() throws Exception{
			ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		pd.put("USER_ID",shopUser.get("USER_ID"));
		List<PageData>	addrlist = addressService.listAll(pd);
		mv.setViewName("address/index");
		mv.addObject("addrlist", addrlist);
		mv.addObject("pd", pd);
		return mv;
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/address/goAdd")
	public ModelAndView goAdd()throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		mv.setViewName("address/address_edit");
		mv.addObject("msg", "save");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/address/goEdit")
	public ModelAndView goEdit()throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = addressService.findById(pd);	//根据ID读取
		mv.setViewName("address/content");
		mv.addObject("msg", "edit");
		mv.addObject("pd", pd);
		return mv;
	}	
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/app/address/add", produces = "application/json;charset=UTF-8")
	public String appadd() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime = sf.format(new Date());
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		PageData pd = new PageData();
		pd = this.getPageData();
		String IS_DEFAULT = pd.getString("IS_DEFAULT");
		
		pd.put("ADDTIME", addtime);
		pd.put("USER_ID",shopUser.get("USER_ID"));
		pd.put("ADDRESS_ID", this.get32UUID());	//主键
		if(StringUtils.isNotEmpty(IS_DEFAULT)){//如果新增的地址设置为默认地址，更改用户其他地址为非默认
			addressService.updatedefault(pd);
		}
		addressService.save(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map);  
	}
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/address/list", produces = "application/json;charset=UTF-8")
	public String jsonlist() throws Exception{
		PageData pd = new PageData();
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		pd.put("user_id",shopUser.get("user_id"));
		List<PageData>	addrlist = addressService.listAll(pd);	
		return gson.toJson(addrlist);  
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/app/address/update", produces = "application/json;charset=UTF-8")
	public String addressedit() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String addtime = sf.format(new Date());
		PageData shopUser = StringUtil.shopUser(this.getRequest().getSession());
		pd.put("ADDTIME", addtime);
		pd.put("USER_ID",shopUser.get("USER_ID"));
		String IS_DEFAULT = pd.getString("IS_DEFAULT");
		if(StringUtils.isNotEmpty(IS_DEFAULT)){//如果地址设置为默认地址，更改用户其他地址为非默认
			addressService.updatedefault(pd);
		}
		addressService.edit(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map);  
	}
	
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value="/app/address/delete", produces = "application/json;charset=UTF-8")
	public String appdelete() throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		addressService.delete(pd);
		map.put("result", 1);
        map.put("message", "提交成功！");
        return gson.toJson(map);  
	}
	
}
