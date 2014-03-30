OOP
===

OOP subject project.
Group 9
Project description:
ĐỀ TÀI 8: CHƯƠNG TRÌNH THỐNG KÊ TỪ VỰNG
Hệ	thống	cho	phép	người	dùng	chọn	đầu	vào	là	file	PDF	hoặc	CHM	(không	được	là	file	TXT	văn	
bản	thuần	túy).	Chương	trình	sẽ	thống	kê	các	từ	xuất	hiện	trong	văn	bản,	bỏ	qua	các	từ	là	tên	
riêng;	viết	tắt;	các	chữ	số.
•  Với	 những	 từ	 gần	 giống	 nhau	 thì	 chỉ	 hiện	 ra	 một	 từ	 đại	 diện.	 Ví	 dụ	 như	 các	 từ:	
technology,	technological,	thì	chỉ	cần	hiện	một	trong	số	chúng	là	được.	
•  Hiển	thị	số	lần	từ	đó	xuất	hiện.	Nếu	từ	technology	xuất	hiện	100	lần	và	từ	technological	
xuất	hiện	200	lần	thì	hiển	thị	tổng	số	lần	xuất	hiện	của	chúng	là	300	lần.	
•  Cho	phép	người	dùng	chơi	đố	chữ.	Danh	sách	các	từ	trong	các	câu	đó	không	được	phép	
xuất	hiện	trong	số	3000	từ	phổ	biến.	Danh	sách	3000	từ	phổ	biến	có	thể	lấy	nguồn	từ
Internet.	Trò	chơi	tiến	hành	như	sau:	Với	một	từ	"adolescent",	hiển	thị	ra	5	câu	có	chỗ
trống.	Chương	trình	sẽ	yêu	cầu	người	dùng	điền	từ	adolescent	vào	đúng	câu	phù	hợp.	
Danh	sách	các	câu	có	thể	lấy	trong	file	PDF	hoặc	CHM.
•  Chương	trình	tính	điểm;	cho	phép	lưu	trữ	điểm	cho	các	người	dùng	khác	nhau.
Chú	ý:
•  Phải	có	dữ	liệu	lưu	trữ	3000	từ	phổ	biến,	có	thể	không	cần	lưu	trữ	nghĩa	của	từ.
•  Đầu	vào	bắt	buộc	phải	là	file	PDF	hoặc	CHM,	trong	đó	có	các	hình	ảnh,	các	từ	viết	tắt,	
các	tên	riêng	(tên	riêng	có	thể	chỉ	cần	xử	lý	các	tên	tiếng	Anh).
•  Giảng	viên	được	quyền	kiểm	tra	phần	mềm	với	các	file	PDF	của	mình	(lấy	từ	các	bài	
báo,	luận	văn,	các	sách	tham	khảo...)
•  Kiểm	 tra	 các	 từ	giống	 nhau,	 chẳng	 hạn	 như	 technology	 hoặc	 technological	 có	 thể	sử
dụng	 thuật	 toán	 LCS	 (Xâu	 con	 chung	 dài	 nhất),	 hoặc	tiến	 hành	 gửi	yêu	 cầu	đến	 một	
trang	từ	điển	(oxforddictionary.com)	để	lấy	danh	sách	các	từ	gần	đúng.
