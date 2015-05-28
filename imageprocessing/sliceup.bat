

echo should already have original image in folder, as well as folders named tiles and samples

basename=groundfloor
filename=groundfloor.jpg
extension=jpg

imagemagick=/opt/ImageMagick/bin/convert
tilesize=256
samplesize=500

tilesfolder=tiles
samplesfolder=samples

echo create tile folders
mkdir -p "$tilesfolder/$basename/"
mkdir -p "$tilesfolder/$basename/1000"
mkdir -p "$tilesfolder/$basename/500"
mkdir -p "$tilesfolder/$basename/250"
mkdir -p "$tilesfolder/$basename/125"
mkdir -p "$samplesfolder"

echo "create half-sized versions for tiling will be discarded later"
$imagemagick $filename -resize 50%  $basename-500.$extension
$imagemagick $filename -resize 25%  $basename-250.$extension
$imagemagick $filename -resize 12.5%  $basename-125.$extension

echo create sample
$imagemagick $filename -thumbnail $samplesizex$samplesize  ./$samplesfolder/$filename

echo create tiles
echo murray $imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/125/%[filename:tile].$extension"
$imagemagick $filename -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/1000_%[filename:tile].$extension"
$imagemagick $basename-500.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/500_%[filename:tile].$extension"
$imagemagick $basename-250.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/250_%[filename:tile].$extension"
$imagemagick $basename-125.$extension -crop 256x256 -set filename:tile "%[fx:page.x/$tilesize]_%[fx:page.y/$tilesize]" +repage +adjoin "./$tilesfolder/$basename/125_%[filename:tile].$extension"


echo cleanup
rm $basename-500.$extension
rm $basename-250.$extension
rm $basename-125.$extension



