//
//  DiscoverCouponView.swift
//  PointExchange
//
//  Created by yiner on 2018/8/11.
//  Copyright © 2018年 WannaWin. All rights reserved.
//

import UIKit

class DiscoverCouponView: UIScrollView {
	var viewDelegate:DiscoverCouponViewDelegate?
	@IBOutlet weak var imageView1: GradientView!
	@IBOutlet weak var imageView3: GradientView!
	@IBOutlet weak var imageView2: GradientView!
	@IBOutlet var view: UIScrollView!
	@IBOutlet weak var image1: UIImageView!
	
	@IBOutlet weak var image2: UIImageView!
	
	@IBOutlet weak var image3: UIImageView!
	
	@IBOutlet weak var contentView: UIView!
	@IBOutlet weak var viewWidthCons: NSLayoutConstraint!
	var images = [UIImageView]()
	var imageViews = [GradientView]()
	
	
	override init(frame: CGRect) {
		super.init(frame: frame)
		initViewFromNib()
	}
	
	required init(coder aDecoder: NSCoder) {
		super.init(coder: aDecoder)!
		initViewFromNib()
	}
	
	private func initViewFromNib(){
		let bundle = Bundle(for: type(of: self))
		let nib = UINib(nibName: "DiscoverCouponView", bundle: bundle)
		self.view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView as! UIScrollView
		self.view.frame = bounds
		self.addSubview(view)
		self.imageViews.append(imageView1)
		self.imageViews.append(imageView2)
		self.imageViews.append(imageView3)
		self.images.append(self.image1)
		self.images.append(self.image2)
		self.images.append(self.image3)
		
		// 添加点击
		viewDelegate?.addTapImageAction()
		
		// contentView
		if viewWidthCons != nil {
			contentView.removeConstraint(viewWidthCons)
		}
		contentView.snp.remakeConstraints{ make in
			make.width.equalTo(view.bounds.size.width+150)
		}
		
		
	}
	
	
}

protocol DiscoverCouponViewDelegate {
	func addTapImageAction()
}
