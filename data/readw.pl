open (FF, "<datafile") or die "cant open";
$count = 0;

while (<FF>) {
	chomp ($_);
	if ($_ =~ m/.W/)
		{print <FF>}
	$count++;
	if ($count == 5)
		{last;}
}
