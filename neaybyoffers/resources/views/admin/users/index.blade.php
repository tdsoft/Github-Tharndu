@foreach ($users as $user)
	<li>{!!$user['fname']!!} {!!$user['lname']!!}</li>
@endforeach