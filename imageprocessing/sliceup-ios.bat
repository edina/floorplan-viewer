

echo should already have original image in folder, as well as folders named tiles and samples


 
basename=groundfloor
filename=groundfloor.jpg
extension=jpg
outextension=png

imagemagick=/opt/ImageMagick/bin/convert
tilesize=256
samplesize=500

tilesfolder=./tiles
samplesfolder=./samples

echo delete tiles folder
echo rm -rf $tilesfolder

echo create tile folders
mkdir -p "$tilesfolder/$basename/"

mkdir -p "$samplesfolder"

echo "create half-sized versions for tiling will be discarded later"
echo $imagemagick $filename -resize 50%  $basename-500.$extension
echo $imagemagick $filename -resize 25%  $basename-250.$extension
echo $imagemagick $filename -resize 12.5%  $basename-125.$extension

echo create sample
$imagemagick $filename -thumbnail $samplesizex$samplesize  ./$samplesfolder/$filename

echo create tiles
echo murray $imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/125/%[filename:tile].$extension"
$imagemagick $filename -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/gf_8x_%[filename:tile].$outextension"
$imagemagick $basename-500.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/gf_4x_%[filename:tile].$outextension"
$imagemagick $basename-250.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/gf_2x_%[filename:tile].$outextension"
$imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/gf_1x_%[filename:tile].$outextension"


echo cleanup




