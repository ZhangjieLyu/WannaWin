//
//  ExchangeViewController.swift
//  PointExchange
//
//  Created by yiner on 2018/7/7.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import UIKit
import SwiftyJSON

class ExchangeViewController: UIViewController, UITableViewDelegate, UITableViewDataSource, ExchangeItemCellDelegate {
	
	var dataSource:[Card]?
	
	@IBOutlet weak var tableView: UITableView!
	@IBOutlet weak var pointsSumLabel: UILabel!
	var pointsSum:Double = 0.0
	
	override func viewDidLoad() {
        super.viewDidLoad()
		self.tableView.delegate = self
		self.tableView.dataSource = self
		if User.getUser().card == nil {
			ServerConnector.getCardCount{ (result,num) in
				if result {
					ServerConnector.getMostPointCards(n: num){(result,cards) in
						if result{
							User.getUser().card = cards
							self.dataSource = cards
							self.tableView.reloadData()
						}
					}
				}
			}
		}else{
			dataSource = User.getUser().card
		}
		
		// 加入“全选”按钮在导航栏右边
		let selectBtn = UIBarButtonItem(title: "全选", style: .plain, target: self, action: #selector(ExchangeViewController.selectAllCell))
		self.navigationItem.rightBarButtonItem = selectBtn
		
		// 设置初始总积分数
		pointsSumLabel.text = String(format:"%.2f", pointsSum)
        
        // 设置键盘弹出收回通知
        NotificationCenter.default.addObserver(self, selector: #selector(ExchangeViewController.keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ExchangeViewController.keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
		
    }

    // MARK: - Table view data source
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
		if dataSource != nil {
			return (dataSource?.count)!
		}else {
			return 0
		}
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
		let cell:UITableViewCell!
		// TODO: - 测试使用，后面再修改
		cell = tableView.dequeueReusableCell(withIdentifier: "store to bank", for: indexPath)
		for subview in cell.contentView.subviews{
			if subview .isKind(of: ExchangeItemCellView.self){
				let exchangeItemCellView = subview as! ExchangeItemCellView
				exchangeItemCellView.perform(#selector(ExchangeItemCellView.setTextFieldDelegateWith), with: self)
				exchangeItemCellView.delegate = self
				exchangeItemCellView.editSourcePoints?.tag = indexPath.row
				if let card = dataSource?[indexPath.row]{
					exchangeItemCellView.storeName.text = card.merchant?.name
					exchangeItemCellView.sourcePoints.text = String(format:"%.2f", card.points)
					exchangeItemCellView.editSourcePoints.placeholder = String(format:"%.2f", card.points)
					exchangeItemCellView.editSourcePoints.text = String(format:"%.2f", card.points)
					exchangeItemCellView.targetPoints.text = String(format:"%.2f", card.points * (card.proportion)!)
					exchangeItemCellView.proportion = card.proportion
				}
			}
		}
		return cell
    }
	
	// MARK: - TextField delegate
    /// 检测输入正确性
	func textFieldShouldEndEditing(_ textField: UITextField) -> Bool{
		let number = Double(textField.text!)
		var maxPoints:Double!
		if let card = self.dataSource?[textField.tag] {
			maxPoints = card.points
		}
		
		if number != nil && number! <= maxPoints {
			return true
		}
		else {
			textField.shake(direction: .horizontal, times: 5, duration: 0.05, delta: 2, completion: nil)
            textField.text = String(format:"%.2f", maxPoints)
			return false
		}
	}
    
    /// 键盘出现视图向上移动
    @objc func keyboardWillShow(notification:NSNotification) {
        
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            self.tableView.contentInset.bottom = keyboardSize.size.height + 60
        }
    }
    
    /// 键盘收回视图向下移动
    @objc func keyboardWillHide(notification:NSNotification) {
        if ((notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue) != nil {
            self.tableView.contentInset.bottom = 0
        }
    }
    
	
	//MARK: - Target Action
    /// 全选积分项
	@objc func selectAllCell() {
        for cell in tableView.visibleCells {
            for subview in cell.contentView.subviews{
                if subview .isKind(of: ExchangeItemCellView.self){
                    let exchangeItemCellView = subview as! ExchangeItemCellView
                    exchangeItemCellView.perform(#selector(ExchangeItemCellView.checkboxClick), with: exchangeItemCellView.checkbox)
                }
            }
        }
        if self.navigationItem.rightBarButtonItem?.title == "全选" {
            self.navigationItem.rightBarButtonItem?.title = "全不选"
        }
        else {
            self.navigationItem.rightBarButtonItem?.title = "全选"
        }
        
	}

	// MARK: - ExchangeItemCell delegate
	/// 获得输入框值并统计积分总数
	func contentDidChanged(text: String, row: Int, type: changeType) {
		switch type {
		case .add:
			pointsSum += Double(text)!
			pointsSumLabel.text = String(format:"%.2f", pointsSum)
			
		default: //minus
			pointsSum -= Double(text)!
			pointsSumLabel.text = String(format:"%.2f", pointsSum)
			let indexPath = IndexPath(row: row, section: 0)
			let cell = self.tableView.cellForRow(at: indexPath)
			for subview in (cell?.contentView.subviews)!{
				if subview .isKind(of: ExchangeItemCellView.self){
					let exchangeItemCellView = subview as! ExchangeItemCellView
					if let card = dataSource?[row]{
						exchangeItemCellView.sourcePoints.text = String(format:"%.2f", card.points)
						exchangeItemCellView.editSourcePoints.placeholder = String(format:"%.2f", card.points)
						exchangeItemCellView.targetPoints.text = String(format:"%.2f", card.points * (card.proportion)!)
					}
				}
			}
		}
	}

	// TODO: - 进行网络请求
	override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
		let isSuccess = true
		// ...
		if isSuccess {
			//准备“兑换成功”数据

			
		}
		else {
			//准备“兑换失败”数据
		}
		
	}
	
}


