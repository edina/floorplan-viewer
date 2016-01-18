

echo should already have original image in folder, as well as folders named tiles and samples


 
basename=floorplan
filename=floorplan.jpg
extension=jpg
outextension=png

imagemagick=/opt/ImageMagick/bin/convert
tilesize=256
samplesize=500

tilesfolder=../app/src/main/assets/tiles
samplesfolder=../app/src/main/assets/tiles/samples

echo delete tiles folder
rm -rf $tilesfolder

echo create tile folders
mkdir -p "$tilesfolder/$basename/"
mkdir -p "$tilesfolder/$basename/1000"
mkdir -p "$tilesfolder/$basename/500"
mkdir -p "$tilesfolder/$basename/250"
mkdir -p "$tilesfolder/$basename/125"
mkdir -p "$samplesfolder"

echo "create half-sized versions for tiling will be discarded later"
sleep 20
#$imagemagick $filename -resize 50%  $basename-500.$extension
sleep 20
$imagemagick $filename -resize 25%  $basename-250.$extension
sleep 20
$imagemagick $filename -resize 12.5%  $basename-125.$extension

echo create sample
sleep 5
$imagemagick $filename -resize 50%  ./$samplesfolder/$filename

echo create tiles
echo murray $imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/125/%[filename:tile].$extension"
$imagemagick $filename -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/1000/_%[filename:tile].$outextension"
$imagemagick $basename-500.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/500/_%[filename:tile].$outextension"
$imagemagick $basename-250.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/250/_%[filename:tile].$outextension"
$imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/125/_%[filename:tile].$outextension"


echo cleanup




