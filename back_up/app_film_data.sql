USE [master]
GO
/****** Object:  Database [APP_FILM]    Script Date: 11/8/2019 4:37:46 PM ******/
CREATE DATABASE [APP_FILM]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'APP_FILM', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\APP_FILM.mdf' , SIZE = 3264KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'APP_FILM_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\APP_FILM_log.ldf' , SIZE = 816KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [APP_FILM] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [APP_FILM].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [APP_FILM] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [APP_FILM] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [APP_FILM] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [APP_FILM] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [APP_FILM] SET ARITHABORT OFF 
GO
ALTER DATABASE [APP_FILM] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [APP_FILM] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [APP_FILM] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [APP_FILM] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [APP_FILM] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [APP_FILM] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [APP_FILM] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [APP_FILM] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [APP_FILM] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [APP_FILM] SET  ENABLE_BROKER 
GO
ALTER DATABASE [APP_FILM] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [APP_FILM] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [APP_FILM] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [APP_FILM] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [APP_FILM] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [APP_FILM] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [APP_FILM] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [APP_FILM] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [APP_FILM] SET  MULTI_USER 
GO
ALTER DATABASE [APP_FILM] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [APP_FILM] SET DB_CHAINING OFF 
GO
ALTER DATABASE [APP_FILM] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [APP_FILM] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [APP_FILM] SET DELAYED_DURABILITY = DISABLED 
GO
USE [APP_FILM]
GO
/****** Object:  Table [dbo].[ACCOUNT]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[ACCOUNT](
	[username_] [char](36) NOT NULL,
	[password_] [char](36) NOT NULL,
	[role] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[username_] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[COMMENT]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[COMMENT](
	[id_comment] [char](15) NOT NULL,
	[id_film] [char](10) NOT NULL,
	[username_] [char](36) NOT NULL,
	[content] [nvarchar](1000) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id_comment] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[FILM]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[FILM](
	[id_film] [char](10) NOT NULL,
	[title_film] [nvarchar](100) NULL,
	[thumbnail] [nvarchar](300) NULL,
	[film_description] [nvarchar](1000) NULL,
	[trailer_url] [nvarchar](300) NULL,
	[film_url] [nvarchar](300) NULL,
	[film_views] [int] NULL,
	[rate_imdb] [float] NULL,
	[genre] [nvarchar](30) NULL,
PRIMARY KEY CLUSTERED 
(
	[id_film] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SAVED_FILM]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SAVED_FILM](
	[id_film] [char](10) NOT NULL,
	[username_] [char](36) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[username_] ASC,
	[id_film] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[USER_PROFILE]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[USER_PROFILE](
	[username_] [char](36) NOT NULL,
	[firstName] [nvarchar](50) NULL,
	[lastName] [nvarchar](50) NULL,
	[avatar] [nvarchar](1000) NULL,
PRIMARY KEY CLUSTERED 
(
	[username_] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[WATCHED_FILM]    Script Date: 11/8/2019 4:37:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[WATCHED_FILM](
	[id_film] [char](10) NOT NULL,
	[cur_progress] [int] NULL,
	[username_] [char](36) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[username_] ASC,
	[id_film] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'                                    ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'admin                               ', N'123                                 ', 1)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'duc123                              ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'duc1234                             ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'duc12345                            ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'hang                                ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'hieu321                             ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long123                             ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long1231                            ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long12312                           ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long1234                            ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long12345                           ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long123456                          ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long1234567                         ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'long321                             ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'luat                                ', N'luat                                ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'newaccount                          ', N'123                                 ', 0)
INSERT [dbo].[ACCOUNT] ([username_], [password_], [role]) VALUES (N'thcuong                             ', N'123                                 ', 0)
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM1            ', N'F01       ', N'duc123                              ', N'Hihi')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM2            ', N'F01       ', N'hieu321                             ', N'Haha')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM3            ', N'F05       ', N'duc123                              ', N'update1')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM4            ', N'F05       ', N'duc123                              ', N'update222')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM5            ', N'F05       ', N'long123                             ', N'update1')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM6            ', N'F01       ', N'long123                             ', N'nice')
INSERT [dbo].[COMMENT] ([id_comment], [id_film], [username_], [content]) VALUES (N'CM7            ', N'F04       ', N'long123                             ', N'phim hay')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F01       ', N'Người nhện', N'SPIDER-MANTrailer(2002).jpg', N'Đây là phim người nhện', N'SPIDER-MANTrailer(2002).mp4', N'SPIDER-MANTrailer(2002).mp4', 8, 8.5, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F010      ', N'Ám ảnh kinh hoàng II', N'AnnabellePart2.jpg', N'Ám ảnh kinh hoàng 2 là bộ phim kinh dị siêu nhiên Mỹ phát hành năm 2016 của đạo diễn James Wan với kịch bản của Carey Hayes, Chad Hayes, Wan và David Leslie Johnson. Đây là phần hai của phim năm 2013, The Conjuring, với Patrick Wilson và Vera Farmiga tiếp tục thủ vai hai thám tử săn ma Ed và Lorraine Warren', N'AnnabellePart2.mp4', N'AnnabellePart2.mp4', 7606, 7.4, N'Horror')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F011      ', N'Ám ảnh kinh hoàng III', N'AnnabellePart3.jpg', N'The Conjuring 3 là một bộ phim kinh dị siêu nhiên sắp ra mắt của Mỹ, được đạo diễn bởi Michael Chaves từ một kịch bản của David Leslie Johnson-McGoldrick. Bộ phim sẽ đóng vai trò là phần tiếp theo của The Conjuring 2013 và The Conjuring 2 năm 2016, và là phần thứ tám trong loạt phim Conjuring Universe.', N'AnnabellePart3.mp4', N'AnnabellePart3.mp4', 8002, 7.4, N'Horror')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F012      ', N'Chú hề ma quái I', N'it1.jpg', N'Chú hề ma quái là một bộ phim kinh dị siêu nhiên Mỹ ra mắt năm 2017 của đạo diễn Andy Muschietti. Đây là phần đầu tiên trong kế hoạch sản xuất loạt phim It hai phần dựa trên cuốn tiểu thuyết cùng tên của nhà văn Stephen King. Nhóm biên kịch của phim gồm có Chase Palmer, Cary Fukunaga và Gary Dauberman', N'it1.mp4', N'it1.mp4', 9103, 7.4, N'Horror')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F013      ', N'Chú hề ma quái II', N'it2.jpg', N'Chú hề ma quái - Phần 2 là một bộ phim kinh dị siêu nhiên sắp ra mắt của Mỹ và là phần tiếp theo của bộ phim It năm 2017. Cả hai bộ phim được dựa trên cuốn tiểu thuyết cùng tên năm 1986 của Stephen King', N'it2.mp4', N'it2.mp4', 9004, 7.4, N'Horror')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F014      ', N'Avengers I', N'TheAvengersPart1.jpg', N'The Avengers là một phim của điện ảnh Hoa Kỳ được xây dựng dựa trên nguyên mẫu các thành viên trong biệt đội siêu anh hùng Avengers của hãng Marvel Comics, sản xuất bởi Marvel Studios và phân phối bởi Walt Disney Studios Motion Pictures. Đây là bộ phim thứ 6 trong Marvel Cinematic Universe', N'TheAvengersPart1.mp4', N'TheAvengersPart1.mp4', 10001, 8.1, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F015      ', N'Avengers II: Đế chế ultron', N'TheAvengersPart2.jpg', N'Avengers: Age of Ultron là một phim của điện ảnh Hoa Kỳ được xây dựng dựa trên nguyên mẫu các thành viên trong biệt đội siêu anh hùng Avengers của hãng Marvel Comics, sản xuất bởi Marvel Studios và phân phối bởi Walt Disney Studios Motion Pictures', N'TheAvengersPart2.mp4', N'TheAvengersPart2.mp4', 12009, 7.3, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F016      ', N'Avengers III: Cuộc chiến vô cực', N'TheAvengersPart3.jpg', N'Avengers: Cuộc chiến vô cực là phim điện ảnh siêu anh hùng Mỹ năm 2018, do Marvel Studios sản xuất và Walt Disney Studios Motion Pictures phân phối. Phim là phần thứ ba của chuỗi phim Avengers, sau Biệt đội siêu anh hùng và Avengers: Đế chế Ultron, đồng thời là phim điện ảnh thứ 19 của Vũ trụ Điện ảnh Marvel', N'TheAvengersPart3.mp4', N'TheAvengersPart3.mp4', 10500, 8.5, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F017      ', N'Avengers IV: Hồi kết', N'TheAvengersPart4.jpg', N'Avengers: Hồi kết là phim điện ảnh siêu anh hùng Mỹ ra mắt năm 2019, do Marvel Studios sản xuất và Walt Disney Studios Motion Pictures phân phối. Phim là phần thứ tư của loạt phim Avengers, sau Biệt đội siêu anh hùng, Avengers: Đế chế Ultron và Avengers: Cuộc chiến vô cực.', N'TheAvengersPart4.mp4', N'TheAvengersPart4.mp4', 15001, 8.7, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F02       ', N'Chiến binh công lý', N'JUSTICELEAGUE.jpg', N'Chiến binh công lý', N'JUSTICELEAGUE.mp4', N'JUSTICELEAGUE.mp4', 2001, 8, N'Science & Fiction')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F03       ', N'Forrest Gump', N'ForrestGump(1994).jpg', N'Forrest là một đứa trẻ không có cha nhưng không phải vì thế mà cậu buồn vì Forrest luôn có tình yêu thương của người mẹ. Cậu bị thiểu năng trí tuệ và lưng không thẳng, phải giữ chân bởi một khung sắt, mẹ đưa cậu vào học một trường học bình thường và ở đây Forrest thường bị mọi người chọc phá. Người bạn duy nhất của Forrest là Jenny, một cô bé ở gần trang trại nơi cậu sống', N'ForrestGump(1994).mp4', N'ForrestGump(1994).mp4', 1008, 8.5, N'Adventure')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F04       ', N'Không gia đình', N'RemiNobodyBoy(2018).jpg', N'Được dịch từ tiếng Anh-Rémi sans famille là một bộ phim hài kịch năm 2018 của đạo diễn Antoine Blossier. Câu chuyện dựa trên tiểu thuyết Sans Famille của tác giả người Pháp Hector Malot', N'RemiNobodyBoy(2018).mp4', N'RemiNobodyBoy(2018).mp4', 2005, 7, N'Adventure')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F05       ', N'Bố già I', N'TheGodfatherPartI.jpg', N'Bố già là một bộ phim hình sự sản xuất năm 1972 dựa theo tiểu thuyết cùng tên của nhà văn Mario Puzo và do Francis Ford Coppola đạo diễn. Phim xoay quanh diễn biến của gia đình mafia gốc Ý Corleone trong khoảng 10 năm từ 1945 đến 1955', N'TheGodfatherPartI.mp4', N'TheGodfatherPartI.mp4', 10004, 9.2, N'Action')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F06       ', N'Bố già II', N'TheGodfatherPartII.jpg', N'Bố già phần II là một bộ phim hình sự sản xuất năm 1974 dựa theo tiểu thuyết cùng tên của nhà văn Mario Puzo và do Francis Ford Coppola đạo diễn. Bộ phim này là phần tiếp theo của tác phẩm nổi tiếng Bố già sản xuất năm 1972 và sau đó được tiếp tục bằng Bố già phần III sản xuất năm 1990', N'TheGodfatherPartII.mp4', N'TheGodfatherPartII.mp4', 10203, 9, N'Action')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F07       ', N'Nhà tù Shawshank', N'TheShawshankRedemption.jpg', N'The Shawshank Redemption là một bộ phim tâm lý của Hoa Kỳ phát hành vào năm 1994, do Frank Darabont viết kịch bản và đạo diễn, dựa trên tiểu thuyết của Stephen King, Rita Hayworth and Shawshank Redemption', N'TheShawshankRedemption.mp4', N'TheShawshankRedemption.mp4', 9010, 9.8, N'Action')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F08       ', N'Gia đình thám tử', N'Shaft(2019).jpg', N'Được dịch từ tiếng Anh-Shaft là một bộ phim hài hành động Mỹ năm 2019 của đạo diễn Tim Story và được viết bởi Kenya Barris và Alex Barnow. Phim có sự tham gia của Samuel L. Jackson, Jessie Usher và Richard Roundtree', N'Shaft(2019).mp4', N'Shaft(2019).mp4', 5008, 6.8, N'Comedy')
INSERT [dbo].[FILM] ([id_film], [title_film], [thumbnail], [film_description], [trailer_url], [film_url], [film_views], [rate_imdb], [genre]) VALUES (N'F09       ', N'Ám ảnh kinh hoàng I', N'AnnabellePart1.jpg', N'The Conjuring là một bộ phim kinh dị siêu nhiên của điện ảnh Hoa Kỳ công chiếu vào năm 2013, đạo diễn bởi James Wan. Hai diễn viên Vera Farmiga và Patrick Wilson vào vai Ed và Lorraine Warren, họ là những nhà điều tra các hiện tượng siêu nhiên xảy ra trên nước Mỹ', N'AnnabellePart1.mp4', N'AnnabellePart1.mp4', 7001, 7.5, N'Horror')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F01       ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F012      ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F03       ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F04       ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F06       ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F07       ', N'duc123                              ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F01       ', N'long123                             ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F02       ', N'long123                             ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F04       ', N'long123                             ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F06       ', N'long123                             ')
INSERT [dbo].[SAVED_FILM] ([id_film], [username_]) VALUES (N'F03       ', N'long321                             ')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'                                    ', N'No', N'Fullname', NULL)
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'duc123                              ', N'Phương', N'Đức', N'duc123.png')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'duc1234                             ', N'No', N'Fullname', N'null')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'duc12345                            ', N'No', N'Fullname', N'duc12345.png')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'hang                                ', N'Thuy', N'Hang', N'hang.png')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'hieu321                             ', N'Minh', N'Hiếu', N'null')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'long123                             ', N'Long', N'Hihi', N'long123.png')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'long321                             ', N'No', N'Fullname', N'null')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'luat                                ', N'Luat', N'hihi', N'null')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'newaccount                          ', N'Sam', N'Smith', N'newaccount.png')
INSERT [dbo].[USER_PROFILE] ([username_], [firstName], [lastName], [avatar]) VALUES (N'thcuong                             ', N'No', N'Fullname', N'null')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F01       ', 498, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F012      ', 93, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F013      ', 127, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F017      ', 65, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F02       ', 62, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F03       ', 50, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F04       ', 17, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F05       ', 64, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F09       ', 83, N'duc123                              ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F01       ', 115, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F013      ', 25, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F015      ', 70, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F02       ', 150, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F03       ', 30, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F04       ', 89, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F08       ', 31, N'long123                             ')
INSERT [dbo].[WATCHED_FILM] ([id_film], [cur_progress], [username_]) VALUES (N'F03       ', 53, N'long321                             ')
ALTER TABLE [dbo].[COMMENT]  WITH CHECK ADD FOREIGN KEY([id_film])
REFERENCES [dbo].[FILM] ([id_film])
GO
ALTER TABLE [dbo].[COMMENT]  WITH CHECK ADD FOREIGN KEY([username_])
REFERENCES [dbo].[ACCOUNT] ([username_])
GO
ALTER TABLE [dbo].[SAVED_FILM]  WITH CHECK ADD FOREIGN KEY([id_film])
REFERENCES [dbo].[FILM] ([id_film])
GO
ALTER TABLE [dbo].[SAVED_FILM]  WITH CHECK ADD FOREIGN KEY([username_])
REFERENCES [dbo].[ACCOUNT] ([username_])
GO
ALTER TABLE [dbo].[USER_PROFILE]  WITH CHECK ADD FOREIGN KEY([username_])
REFERENCES [dbo].[ACCOUNT] ([username_])
GO
ALTER TABLE [dbo].[WATCHED_FILM]  WITH CHECK ADD FOREIGN KEY([id_film])
REFERENCES [dbo].[FILM] ([id_film])
GO
ALTER TABLE [dbo].[WATCHED_FILM]  WITH CHECK ADD FOREIGN KEY([username_])
REFERENCES [dbo].[ACCOUNT] ([username_])
GO
USE [master]
GO
ALTER DATABASE [APP_FILM] SET  READ_WRITE 
GO
