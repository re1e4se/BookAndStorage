# BookAndStorage
BookAndStorage is a Spigot Plugin that I made for fun that lets you store data in Minecraft books.
> [!NOTE]
> There might be bugs, issues etc. that I haven't found out yet. If you find any bugs, feel free to open an issue.
> 
> To get the stable release, you can either use the codebase on stable folder or download the stable jar from releases tab.

## Introduction
Aren't you tired of using Book and Quills for just storing simple things like some text of what you did in your world? If your answer is yes, then BookAndStorage changes that! You can store everything that comes to your mind in books!

## Known Issues
Currently there aren't any known issues.

## Features
- **Data Storage**: Store various types of data in books, such as text, images, small videos, even more!
- **Book Splitting**: Split bigger data to chunks of books! Currently, you can't get more than 36 books of data since books are placed into your inventory. If you try to create data worth more than 36 books (approximately 288 KB), you will not receive the rest of the data.
- **Ease of Use**: You can easily create books with one command or write a content of a book to an actual file!
- **Compatibility**: Compatible with Minecraft version 1.14+ due to increased page sizes of the books.
> [!NOTE]
> If you want to build the plugin for versions below 1.14, see the [Building for Versions Below 1.14](#building-for-versions-below-114) section below.
- **Performance**: I've tested the plugin on a 1.16.5 server with only 1 GB of RAM allocated with no other plugins and while creating 36 books that contain 8 KB data each, TPS was stable 20 all the time.

## Planned Features
- Storing up to 7,992 KB (approximately 8 MB) of data by filling up the whole inventory with shulker boxes that contain chunks of data.
- Limiting data usage for books.
- Changing whether to give player Book and Quill or Written Book from the config file.

## Limitations
- You can only store up to 8 KB files.
- You can't split files into chunks of books.

## Getting Started
To get started with the plugin, simply install it to your server by downloading the plugin from releases tab. You don't need to install any dependency to run the plugin.

## Building for Versions Below 1.14
Do you need to run the plugin on versions below 1.14? No worries, you can still do that. Since I've started developing the plugin for 1.12 and switched to 1.14 because of increased book page limit, the plugin is fully compatible with 1.12.
> [!NOTE]
> To build from the stable release, you need to build the project from the `stable` directory.

To build the plugin for versions below 1.14, you can follow the following steps:
1. Clone the repository:
```
git clone https://github.com/re1e4se/BookAndStorage.git
```
2. Change `api-version` in `spigot.yml` file which is located at `/src/main/resources` to your target version such as `1.12`
3. Change dependency version in `pom.xml` file which is located at the root folder of the project to your target dependency version such as `1.12-R0.1-SNAPSHOT` It should look like following:
```xml
<dependencies>
  <dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.12-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```
> [!NOTE]
> This step is only valid if you use the latest codebase, if you use stable release skip to step 5.
4. Change book page limit in `config.yml` to avoid any errors that might occur when creating a book that has more than 50 pages (approximately 4 KB) data in it to `50`. It should look like following:
```yml
bookPageLimit: 50
```
> [!NOTE]
> You don't need to do this step if you use the latest codebase.
5. Change book page limit in `CreateDataCommand.java` to avoid any errors that might occur when creating a book that has more than 50 pages (approximately 4 KB) data in it to `50`. It should look like following:
```java
int bookPageSize = 50;
```
